package com.wang.base.config.security;

import com.alibaba.fastjson.JSON;
import com.wang.base.common.utils.ResultUtil;
import com.wang.base.common.utils.RedisUtil;
import com.wang.base.common.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * @ClassName:
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@Component
@Slf4j
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String authToken = authHeader.substring("Bearer ".length());
            if(redisUtil.hasToken(authToken)) {
                //将token放入黑名单中
                redisUtil.addBlackList(authToken);
                //删除旧的token保存的redis
                redisUtil.deleteToken(authToken);
            }
            log.info("用户登出成功！token：{}已加入redis黑名单",authToken);
        }
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpServletResponse.getWriter().println(JSON.toJSONString(ResultUtil.success(ResultEnum.USER_LOGOUT_SUCCESS.getMessage())));
    }

}
