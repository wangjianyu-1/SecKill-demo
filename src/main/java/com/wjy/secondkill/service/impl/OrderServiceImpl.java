package com.wjy.secondkill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjy.secondkill.entity.Order;
import com.wjy.secondkill.entity.SeckillGoods;
import com.wjy.secondkill.entity.SeckillOrder;
import com.wjy.secondkill.entity.User;
import com.wjy.secondkill.mapper.OrderMapper;
import com.wjy.secondkill.service.IOrderService;
import com.wjy.secondkill.service.ISeckillGoodsService;
import com.wjy.secondkill.service.ISeckillOrderService;
import com.wjy.secondkill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王建宇
 * @since 2022-01-23
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public Order secKill(User user, GoodsVo goods) {
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq
                ("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        //seckillGoodsService.updateById(seckillGoods);
        //解决超卖问题
        boolean result = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = stock_count-1").
                eq("goods_id", goods.getId()).gt("stock_count", 0));

        if(!result){
            //判断库存为0
            redisTemplate.opsForValue().set("isStockEmpty:"+goods.getId(),"0");
            return null;
        }

        Order order = new Order();
        order.setCreateDate(new Date());
        order.setDeliveryAddrId(0L);
        order.setGoodsCount(1);
        order.setGoodsId(goods.getId());
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsPrice(goods.getGoodsPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setUserId(user.getId());
        //生成订单
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrderService.save(seckillOrder);

        //订单存到redis缓存中
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(),seckillOrder,60, TimeUnit.SECONDS);

        return order;
    }
}
