package com.wang.base.config.loginTime;

import com.wang.base.config.redis.RedisUtil;
import com.wang.base.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * 登陆成功监听
 *
 * @author Shaoj 3/2/2017.
 */
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    RedisUtil redisUtil;
 
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        User yftUserDetails = (User) authenticationSuccessEvent.getAuthentication().getPrincipal();
        String account = yftUserDetails.getUsername();
        String key = "user_" + account;
        redisUtil.delete(key);
    }
}