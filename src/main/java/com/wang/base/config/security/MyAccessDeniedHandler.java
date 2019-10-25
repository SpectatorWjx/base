package com.wang.base.config.security;

import com.alibaba.fastjson.JSON;
import com.wang.base.common.utils.ResultUtil;
import com.wang.base.common.enums.ResultEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * @ClassName: 无权访问
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpServletResponse.getWriter().println(JSON.toJSONString(ResultUtil.exception(ResultEnum.USER_NO_ACCESS.getCode(),ResultEnum.USER_NO_ACCESS.getMessage())));
    }
}
