package com.wang.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFailureHandler
  implements AuthenticationFailureHandler {

    @Autowired
    RedisTemplate stringRedisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();
 
    @Override
    public void onAuthenticationFailure(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception)
      throws IOException{
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> data = new HashMap<>();
        data.put("status",401);
        if (exception instanceof InternalAuthenticationServiceException) {
            data.put("message", exception.getMessage());
        } else {
            data.put("message","用户名或密码错误");
        }
        response.getWriter().println(objectMapper.writeValueAsString(data));
    }
}