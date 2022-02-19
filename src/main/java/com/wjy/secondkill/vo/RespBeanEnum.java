package com.wjy.secondkill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Author: 爱吃羊的大灰狼
 * @date: 2022/1/26 16:39
 */
@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {

    SUCCESS(200,"success"),
    ERROR(500,"服务端异常"),
    LOGINVO_ERROR(50020,"用户名或密码错误"),
    LOGIN_ERROR(500210, "用户名或密码不正确"),
    MOBILE_ERROR(500211, "手机号码格式不正确"),
    BINDING_ERROR(500212,"参数校验异常"),
    EMPTY_STOCK(500500, "库存不足"),
    REPEATE_ERROR(500501, "该商品每人限购一件"),
    queueing(500503, "排队中");

    private final Integer Code;
    private final String message;
}
