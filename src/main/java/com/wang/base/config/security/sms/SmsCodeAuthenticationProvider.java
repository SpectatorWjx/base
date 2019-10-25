package com.wang.base.config.security.sms;

import com.wang.base.common.enums.ResultEnum;
import com.wang.base.common.utils.RedisUtil;
import com.wang.base.common.utils.StringUtil;
import com.wang.base.config.SecurityUserService;
import com.wang.base.dao.UserJpa;
import com.wang.base.model.User;
import com.wang.base.model.UserEntity;
import com.wang.base.service.user.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;


@Data
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    UserJpa userJpa;

    @Autowired
    UserService userService;

    @Autowired
    private SecurityUserService securityUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;

        String mobile = (String) authenticationToken.getPrincipal();

        String inputCode = authenticationToken.getSmsCode();

        if(redisUtil.hasPhoneMsg(mobile)) {
            String redisCode = redisUtil.getPhoneMsg(mobile).toString();
            if(StringUtil.isEmpty(redisCode)){
                throw new InternalAuthenticationServiceException(ResultEnum.SMS_CODE_IS_INVALID.getMessage());
            }
            //校验验证码
            if(!redisCode.equals(inputCode)){
                throw new InternalAuthenticationServiceException(ResultEnum.SMS_CODE_ERROR.getMessage());
            }
        } else {
            throw new InternalAuthenticationServiceException(ResultEnum.SMS_CODE_NOT_SEND.getMessage());
        }
        UserEntity user = userJpa.findByPhone(mobile);
        if (user == null) {
            userService.register(mobile);
        }
        //校验手机号
        UserDetails userDetails = securityUserService.loadUserByUsername(mobile);

        redisUtil.deletePhoneMsg(mobile);

        //这时候已经认证成功了
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
