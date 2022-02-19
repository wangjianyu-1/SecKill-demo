package com.wjy.secondkill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjy.secondkill.entity.User;
import com.wjy.secondkill.vo.LoginVo;
import com.wjy.secondkill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王建宇
 * @since 2022-01-28
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);
}
