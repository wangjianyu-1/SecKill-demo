package com.wjy.secondkill.rabbitMq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 爱吃羊的大灰狼
 * @date: 2022/2/15 12:13
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送秒杀信息
     * @param msg
     */
    public void sendMessage(String msg){
        log.info("发送消息："+msg);
        rabbitTemplate.convertAndSend("secKillExchange","secKill.message",msg);
    }
}
