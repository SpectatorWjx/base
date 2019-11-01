package com.wang.base.common.utils;

import com.wang.base.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/***
 * @ClassName: SecurityAuthUtil
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/31 16:54
 */
public class SecurityAuthUtil {

    public static User getPrincipal(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getPrincipal() instanceof String){
            return null;
        }
        User user = (User)auth.getPrincipal();
        return user;
    }

    public static String getUsername(){
        User principal = getPrincipal();
        if(Optional.ofNullable(principal).isPresent()) {
            return principal.getUsername();
        }
        return null;
    }

    public static String getUserId(){
        User principal = getPrincipal();
        if(Optional.ofNullable(principal).isPresent()) {
            return principal.getUserId();
        }
        return null;
    }
}
