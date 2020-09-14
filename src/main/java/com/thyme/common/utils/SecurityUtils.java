package com.thyme.common.utils;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author thyme
 * @ClassName SecurityUtils
 * @Description TODO
 * @Date 2019/12/24 14:49
 */
public class SecurityUtils {

    /**
     * 获取当前用户
     */
    public static Authentication getCurrentUserAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
    /*
     *
     * @Date 2020/6/1 1:48
     * @Param []
     * @return 返回当前登入者token(可用于获取用户名)
     **/
    public static AbstractAuthenticationToken getCurrentUserToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            return token;
        }else if (authentication instanceof RememberMeAuthenticationToken){
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) authentication;
            return token;
        }else {
            return null;
        }
    }
}
