package com.wang.base.common.log;

import lombok.Data;

/**
 * The type Logs entity.
 *
 * @author : R.lee
 * @date : 2019.08.26 16:01:45
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
