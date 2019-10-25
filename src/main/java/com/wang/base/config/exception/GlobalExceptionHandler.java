package com.wang.base.config.exception;

import com.wang.base.common.exception.BaseException;
import com.wang.base.common.result.Result;
import com.wang.base.common.utils.ResultUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.xml.ws.handler.Handler;


@ControllerAdvice
public class GlobalExceptionHandler implements HandlerInterceptor {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result defaultErrorHandler(Exception ex) {
        if (ex instanceof BaseException) {
            return ResultUtil.exception(((BaseException) ex).getErrorCode(), ((BaseException) ex).getErrorMessage());
        } else if (ex instanceof RuntimeException) {
            ex.printStackTrace();
            return ResultUtil.exception(500, "运行时异常:" + ex.toString() + ":" + ex.getMessage());
        } else {
            ex.printStackTrace();
            return ResultUtil.exception(500, "系统错误:" + ex.toString() + ":" + ex.getMessage());
        }

    }

}