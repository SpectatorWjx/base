package com.wang.base.config.security;

import com.alibaba.fastjson.JSON;
import com.wang.base.common.utils.*;
import com.wang.base.common.enums.ResultEnum;
import com.wang.base.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/***
 * @ClassName:
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@Component
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${token.expirationSeconds}")
    private int expirationSeconds;

    @Value("${token.validTime}")
    private int validTime;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //获取请求的ip地址
        String clientId = IpUtil.getIpAddr(httpServletRequest);
        Map<String,Object> map = new HashMap<>();
        map.put("clientId",clientId);

        User userDetails = (User) authentication.getPrincipal();

        String jwtToken = JwtTokenUtil.generateToken(userDetails.getUsername(), expirationSeconds, map);

        redisUtil.setTokenRefresh(jwtToken,userDetails.getUsername(),clientId);
        log.info("用户{}登录成功，信息已保存至redis",userDetails.getUsername());

        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpServletResponse.setHeader("Authorization", jwtToken);
        httpServletResponse.getWriter().println(JSON.toJSONString(ResultUtil.success(ResultEnum.SUCCESS.getMessage())));
    }
}
