package com.wjy.secondkill.controller;

import com.wjy.secondkill.entity.User;
import com.wjy.secondkill.rabbitMq.MQSender;
import com.wjy.secondkill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: 爱吃羊的大灰狼
 * @date: 2022/2/11 21:25
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MQSender mqSender;

    /**
     * 优化前 2576
     * @param user
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user){
        return RespBean.success(user);
    }

    @RequestMapping("/mq")
    @ResponseBody
    public void mq(){
        mqSender.sendMessage("Hello");
    }

    @RequestMapping("/mqFanout")
    @ResponseBody
    public void mqFanout(){
        mqSender.sendMessage("Hello");
    }
}
