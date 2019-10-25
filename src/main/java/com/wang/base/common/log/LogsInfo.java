package com.wang.base.common.log;

import lombok.Data;

/***
 * @ClassName:
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@Data
public class LogsInfo{

    /**
     * 用户
     */
    private String userName;
    /**
     * 用户Ip
     */
    private String userIp;
    /**
     * 用户代理
     */
    private String userAgent;
    /**
     * 请求类型
     */
    private String method;
    /**
     * 异常信息
     */
    private String exception;
    /**
     * 参数
     */
    private String params;
    /**
     * 状态
     */
    private int status;
    /**
     * 请求Url
     */
    private String requestUrl;
    /**
     * Java类
     */
    private String javaClass;

}
