package com.wjy.secondkill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjy.secondkill.entity.SeckillOrder;
import com.wjy.secondkill.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王建宇
 * @since 2022-01-23
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    Long getResult(User user, Long goodsId);
}
