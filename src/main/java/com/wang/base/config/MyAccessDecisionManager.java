package com.wang.base.config;

import com.alibaba.druid.util.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

@Component
public class MyAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) {

        Iterator<ConfigAttribute> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (authentication == null) {
                throw new AccessDeniedException("当前访问没有权限");
            }
            ConfigAttribute configAttribute = iterator.next();
            String needCode = configAttribute.getAttribute();
            if(StringUtils.isEmpty(needCode)){
                return;
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (StringUtils.equals(authority.getAuthority(), needCode)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("当前访问没有权限");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}