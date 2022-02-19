package com.wjy.secondkill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wjy.secondkill.entity.Order;
import com.wjy.secondkill.entity.SeckillMessage;
import com.wjy.secondkill.entity.SeckillOrder;
import com.wjy.secondkill.entity.User;
import com.wjy.secondkill.rabbitMq.MQSender;
import com.wjy.secondkill.service.IGoodsService;
import com.wjy.secondkill.service.IOrderService;
import com.wjy.secondkill.service.ISeckillOrderService;
import com.wjy.secondkill.utils.JsonUtil;
import com.wjy.secondkill.vo.GoodsVo;
import com.wjy.secondkill.vo.RespBean;
import com.wjy.secondkill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 爱吃羊的大灰狼
 * @date: 2022/2/10 11:27
 */
@Slf4j
@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    private Map<Long,Boolean> emptyStockMap = new HashMap<>();

    /**
     * 优化前：windows 499
     * 加入redis保存订单信息：806
     * 加入消息队列优化：1251
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/doSeckill")
    public String doSecKill(Model model, User user, Long goodsId){
        if(user == null){
            return "login";
        }

//        model.addAttribute("user",user);
//        GoodsVo goods = goodsService.findGoodsVoById(goodsId);
//        //判断库存
//        if(goods.getStockCount() < 1){
//            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
//            return "secKillFail";
//        }
//        //判断是否重复购买
//        QueryWrapper<SeckillOrder> wrapper = new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_Id", goodsId);
//        SeckillOrder seckillOrder = seckillOrderService.getOne(wrapper);

//        //(优化,通过redis缓存去保存和获取，避免访问mysql)
//        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
//
//        if(seckillOrder != null ){
//            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
//            return "secKillFail";
//        }
//        Order order = orderService.secKill(user,goods);
//        model.addAttribute("order",order);
//        model.addAttribute("goods",goods);

//        return "orderDetail";

        /**利用redis+RabbitMQ优化*/
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);

        if(seckillOrder != null ){
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }

        //内存标记，减少对redis的访问
        if(emptyStockMap.get(goodsId)){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }

        //预减库存
        long stock = valueOperations.decrement("seckillgoods:"+goodsId);
        if(stock<0){
            emptyStockMap.put(goodsId,true);

            valueOperations.increment("seckillgoods:"+goodsId);
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        //下单
        SeckillMessage seckillMessage = new SeckillMessage(user,goodsId);
        //发送消息给队列
        mqSender.sendMessage(JsonUtil.object2JsonStr(seckillMessage));

        //提前返回给前端，前端接收，返回正在排队
        model.addAttribute("errmsg", "正在排队");
        model.addAttribute("goodsId", goodsId);
        return "queuing";
    }

    /**
     *
     * @param user
     * @param goodsId:成功  ，  -1：秒杀失败    ，0排队中
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId){
        if(user==null){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user,goodsId);

        return RespBean.success(orderId);
    }





    /**
     * 初始化，将商品库存加载到Redis
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillgoods:"+goodsVo.getId(),goodsVo.getStockCount());
            //将所有的商品id插入内存，设为非空
            emptyStockMap.put(goodsVo.getId(),false);
        });
    }
}
