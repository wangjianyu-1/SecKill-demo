package com.wjy.secondkill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wjy.secondkill.entity.Order;
import com.wjy.secondkill.entity.SeckillGoods;
import com.wjy.secondkill.entity.SeckillOrder;
import com.wjy.secondkill.entity.User;
import com.wjy.secondkill.mapper.SeckillOrderMapper;
import com.wjy.secondkill.service.IGoodsService;
import com.wjy.secondkill.service.IOrderService;
import com.wjy.secondkill.service.ISeckillOrderService;
import com.wjy.secondkill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;

/**
 * @Author: 爱吃羊的大灰狼
 * @date: 2022/2/15 20:48
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IOrderService orderService;

    @RequestMapping("/oderDetail/{orderId}/{goodsId}")
    public String orderDetail(User user, Model model, @PathVariable Long orderId ,@PathVariable Long goodsId){

        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);

        Order order = orderService.getById(seckillOrder.getOrderId());

        GoodsVo goods = goodsService.findGoodsVoById(goodsId);

        model.addAttribute("order",order);
        model.addAttribute("goods",goods);

        return "orderDetail";
    }
}
