package com.wang.base.config.security;

import com.alibaba.fastjson.JSON;
import com.wang.base.common.result.Result;
import com.wang.base.common.utils.ResultUtil;
import com.wang.base.enums.ResultEnum;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * @ClassName: A
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Content-Type", "text/html; charset=UTF-8");
        Result result =  ResultUtil.error(ResultEnum.USER_LOGIN_FAILED.getCode(),ResultEnum.USER_LOGIN_FAILED.getMessage());
        if (e instanceof InternalAuthenticationServiceException) {
            result= ResultUtil.exception(ResultEnum.USER_LOGIN_FAILED.getCode(),e.getMessage());
        }
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }

}
