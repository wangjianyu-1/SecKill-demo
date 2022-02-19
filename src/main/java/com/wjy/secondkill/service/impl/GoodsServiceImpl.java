package com.wjy.secondkill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjy.secondkill.entity.Goods;
import com.wjy.secondkill.mapper.GoodsMapper;
import com.wjy.secondkill.service.IGoodsService;
import com.wjy.secondkill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王建宇
 * @since 2022-01-23
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> findGoodsVo() {

        return goodsMapper.findGoodsVo();

    }

    /**
     * 功能描述： 获取商品详情
     *
     * @param goodsId:  [goodsId]
     * @return com.wjy.secondkill.vo.GoodsVo
     * @author wangjianyu
     * @date 2022/2/9 20:52
     */
    @Override
    public GoodsVo findGoodsVoById(Long goodsId) {

        return goodsMapper.findGoodsVoBygoodsId(goodsId);
    }


}
