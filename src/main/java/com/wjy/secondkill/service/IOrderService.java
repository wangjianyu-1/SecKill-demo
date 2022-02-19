package com.wjy.secondkill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjy.secondkill.entity.Order;
import com.wjy.secondkill.entity.User;
import com.wjy.secondkill.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王建宇
 * @since 2022-01-23
 */
public interface IOrderService extends IService<Order> {

    Order secKill(User user, GoodsVo goods);
}
