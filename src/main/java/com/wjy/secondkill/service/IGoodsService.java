package com.wjy.secondkill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjy.secondkill.entity.Goods;
import com.wjy.secondkill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王建宇
 * @since 2022-01-23
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoById(Long goodsId);
}
