package com.wjy.secondkill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjy.secondkill.Exception.GlobalException;
import com.wjy.secondkill.entity.User;
import com.wjy.secondkill.mapper.UserMapper;
import com.wjy.secondkill.service.IUserService;
import com.wjy.secondkill.utils.CookieUtil;
import com.wjy.secondkill.utils.MD5Util;
import com.wjy.secondkill.utils.UUIDUtil;
import com.wjy.secondkill.vo.LoginVo;
import com.wjy.secondkill.vo.RespBean;
import com.wjy.secondkill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王建宇
 * @since 2022-01-28
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        log.info("手机号："+mobile);
        log.info("密码："+password);

//        if (StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
//            return RespBean.error(RespBeanEnum.LOGINVO_ERROR);
//        }
//        if (!ValidatorUtil.isMobile(mobile)){
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }
        //根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if (null==user){
            log.info("没找到用户");
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
//校验密码
        if
        (!MD5Util.fromPassToDbPass(password,user.getSalt()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //生成cookie
        String ticket = UUIDUtil.uuid();

        //将用户信息存入redis中
        redisTemplate.opsForValue().set("user:"+ticket,user);
        request.getSession().setAttribute(ticket,user);
        CookieUtil.setCookie(request,response,"userTicker",ticket);

        return RespBean.success(ticket);
    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if(userTicket == null){
            return null;
        }
        User user = (User)redisTemplate.opsForValue().get("user:"+userTicket);
        if(user!=null){
            CookieUtil.setCookie(request,response,"userTicker",userTicket);
        }
        return user;
    }
}
