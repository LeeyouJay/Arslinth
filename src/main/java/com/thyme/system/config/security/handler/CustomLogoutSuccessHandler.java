package com.thyme.system.config.security.handler;

import com.thyme.common.base.Constants;
import com.thyme.common.utils.SecurityUtils;
import com.thyme.system.entity.SysUser;
import com.thyme.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author thyme
 * @ClassName CustomLogoutSuccessHandler
 * @Description TODO
 * @Date 2019/12/18 18:00
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${server.servlet.context-path}")
    private String path;

    private final SysLogService sysLogService;


    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //保存日志
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            String username = token.getName();
            sysLogService.saveLoginLog(httpServletRequest,Constants.LOGOUT_SUCCESS,username);
        }else if(authentication instanceof RememberMeAuthenticationToken){
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) authentication;
            String username = token.getName();
            sysLogService.saveLoginLog(httpServletRequest,Constants.LOGOUT_SUCCESS,username);
        }
        httpServletResponse.sendRedirect(path==null? Constants.LOGIN_URL :path + Constants.LOGIN_URL);
    }
}
