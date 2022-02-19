package com.wjy.secondkill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjy.secondkill.entity.SeckillOrder;
import com.wjy.secondkill.entity.User;
import com.wjy.secondkill.mapper.OrderMapper;
import com.wjy.secondkill.mapper.SeckillOrderMapper;
import com.wjy.secondkill.service.ISeckillOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王建宇
 * @since 2022-01-23
 */
@Service
@Slf4j
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 获取秒杀结果
     *
     * @param user
     * @param goodsId:成功  ，  -1：秒杀失败    ，0排队中
     * @return
     */
    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id", user.getId())
                .eq("goods_id", goodsId));
        log.info(" 查询id="+user.getId()+"goodsId="+goodsId+"   订单是否生成成功："+seckillOrder);
        if(seckillOrder!=null){
            return seckillOrder.getOrderId();
        }else if(redisTemplate.hasKey("isStockEmpty:"+goodsId)){
            return -1L;
        }
        else{
            return 0L;
        }

    }
}
