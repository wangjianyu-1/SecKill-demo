package com.wjy.secondkill.controller;

import com.wjy.secondkill.service.IUserService;
import com.wjy.secondkill.vo.LoginVo;
import com.wjy.secondkill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Author: 爱吃羊的大灰狼
 * @date: 2022/1/23 21:20
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService userService;

    /**
     * 功能描述： 
     *
     * @param :  []
     * @return void
     * @author wangjianyu
     * @date 2022/1/23 21:25
     */
    @RequestMapping("/toLogin")
    public String toLogin(LoginVo loginVo){
        return "login";
    }

    /**
     * 功能描述： 登入功能
     *
     * @param loginVo:
     * @return java.lang.String
     * @author wangjianyu
     * @date 2022/1/28 10:48
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        log.info(loginVo.getPassword());
        return userService.doLogin(loginVo, request, response);
    }
}
