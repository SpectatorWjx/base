package com.wang.base.common.result;

import lombok.Data;
/***
 * @ClassName:
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@Data
public class Result<T>{
    /**
     * 错误码
     * */
    private Integer code;

    /**
     * 提示信息
     * */
    private String msg;

    /**
     * 具体内容
     * */
    private  T data;
}