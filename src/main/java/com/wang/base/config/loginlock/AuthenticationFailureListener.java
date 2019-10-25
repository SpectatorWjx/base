package com.wang.base.config.loginlock;

import com.wang.base.common.utils.RedisUtil;
import com.wang.base.dao.UserJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 登陆失败监听
 *
 * @author Shaoj 3/2/2017.
 */
@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserJpa userJpa;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent authenticationFailureBadCredentialsEvent) {
        String username = authenticationFailureBadCredentialsEvent.getAuthentication().getPrincipal().toString();

        if(!Optional.ofNullable(userJpa.findByPhone(username)).isPresent()){
            return;
        }

        String key = "user_" + username;
        boolean hasKey = redisUtil.hasKey(key);
        if (hasKey) {
            Integer count = Integer.parseInt(redisUtil.get(key));
            if(count<5) {
                int newCount = count+1;
                redisUtil.setEx(key, String.valueOf(newCount), redisUtil.getExpire(key,TimeUnit.SECONDS), TimeUnit.SECONDS);
            }
        } else {
            redisUtil.set(key, String.valueOf(1));
            redisUtil.expire(key, 3*60, TimeUnit.SECONDS);
        }
    }
}