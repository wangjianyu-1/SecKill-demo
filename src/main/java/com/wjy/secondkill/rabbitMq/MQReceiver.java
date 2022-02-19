package com.wjy.secondkill.rabbitMq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wjy.secondkill.entity.Order;
import com.wjy.secondkill.entity.SeckillOrder;
import com.wjy.secondkill.entity.User;
import com.wjy.secondkill.service.IGoodsService;
import com.wjy.secondkill.service.IOrderService;
import com.wjy.secondkill.utils.JsonUtil;
import com.wjy.secondkill.vo.GoodsVo;
import com.wjy.secondkill.vo.RespBeanEnum;
import com.wjy.secondkill.vo.SecKillMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 爱吃羊的大灰狼
 * @date: 2022/2/15 12:15
 */
@Slf4j
@Service
public class MQReceiver {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;


    /**
     * 下定操作
     */
    @RabbitListener(queues = "secKillQueue")
    public void receiver(String msg){
        log.info("消息接收"+msg);
        SecKillMessage secKillMessage = JsonUtil.jsonStr2Object(msg,SecKillMessage.class);
        Long goodsId = secKillMessage.getGoodId();
        User user = secKillMessage.getUser();

        GoodsVo goodsVo = goodsService.findGoodsVoById(goodsId);
        //判断库存
        if(goodsVo.getStockCount()<0){
            return;
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder != null ){
            return;
        }

        //下单操作
        Order order = orderService.secKill(user, goodsVo);

    }
}
