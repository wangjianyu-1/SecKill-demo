package com.wjy.secondkill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 爱吃羊的大灰狼
 * @date: 2022/1/26 16:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {

    private long code;
    private String message;
    private Object obj;

    /**
     * 功能描述： 成功的返回结果
     *
     * @param :  []
     * @return com.wjy.secondkill.vo.RespBean
     * @author wangjianyu
     * @date 2022/1/26 16:47
     */
    public static RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(),null);
    }

    /**
     * 功能描述： 
     *
     * @param obj:  [obj]
     * @return com.wjy.secondkill.vo.RespBean
     * @author wangjianyu
     * @date 2022/1/26 16:47
     */
    public static RespBean success(Object obj){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(),obj);
    }

    /**
     * 功能描述：
     *
     * @param respBeanEnum:
     * @return com.wjy.secondkill.vo.RespBean
     * @author wangjianyu
     * @date 2022/1/26 16:49
     */
    public static RespBean error(RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),null);
    }

    /**
     * 功能描述：
     *
     * @param respBeanEnum:
     * @param obj:
     * @return com.wjy.secondkill.vo.RespBean
     * @author wangjianyu
     * @date 2022/1/26 16:49
     */
    public static RespBean error(RespBeanEnum respBeanEnum, Object obj){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),obj);
    }
}
