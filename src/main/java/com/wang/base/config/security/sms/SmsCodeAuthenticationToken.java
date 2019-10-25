package com.wang.base.config.security.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public class SmsCodeAuthenticationToken  extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 420L;
    private final Object principal;
    private  String smsCode;

    SmsCodeAuthenticationToken(String mobile, String smsCode) {
        //因为刚开始并没有认证，因此用户没有任何权限，并且设置没有认证的信息（setAuthenticated(false)）
        super(null);
        //这里的principal就是手机号
        this.principal = mobile;
        this.smsCode = smsCode;
        this.setAuthenticated(false);
    }

    SmsCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getSmsCode() {
        return this.smsCode;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }



}
