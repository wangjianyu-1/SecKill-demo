package com.wjy.secondkill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjy.secondkill.entity.Goods;
import com.wjy.secondkill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 王建宇
 * @since 2022-01-23
 */
@Mapper
@Repository
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     *
     * @return
     */
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoBygoodsId(Long goodsId);
}
