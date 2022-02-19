package com.wjy.secondkill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 爱吃羊的大灰狼
 * @date: 2022/2/15 10:17
 */
@Configuration
public class RabbitMQConfig {

    private static final String QUEUE = "secKillQueue";

    private static final String EXCHANGE = "secKillExchange";

    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("secKill.#");
    }
}
