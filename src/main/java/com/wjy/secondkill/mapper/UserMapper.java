package com.wjy.secondkill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjy.secondkill.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 王建宇
 * @since 2022-01-28
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User>{



}
