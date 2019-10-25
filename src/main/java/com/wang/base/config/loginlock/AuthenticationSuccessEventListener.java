package com.wang.base.config.loginlock;

import com.wang.base.common.utils.RedisUtil;
import com.wang.base.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/***
 * @ClassName:
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
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