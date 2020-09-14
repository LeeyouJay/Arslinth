package com.thyme.system.config.interceptor;

import com.thyme.common.base.Constants;
import com.thyme.common.utils.SecurityUtils;
import com.thyme.system.entity.SysMenu;
import com.thyme.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author thyme
 * @ClassName LogHandlerInterceptor
 * @Description TODO
 * @Date 2020/1/9 16:45
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccessAuthHandlerInterceptor implements HandlerInterceptor {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final SysMenuService sysMenuService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        List<SysMenu> secondMenu = sysMenuService.getSecondMenu();
        List<String> urlList = secondMenu.stream().map(o -> o.getMenuHref()).collect(Collectors.toList());
        String servletPath = StringUtils.replace(request.getServletPath(), "/", "", 1);
        if (urlList.contains(servletPath)) {
            // 判断权限
            String username = SecurityUtils.getCurrentUserToken().getName();
            List<SysMenu> sysMenuList = sysMenuService.findMenuListByUser(username);
            if (sysMenuList.stream().anyMatch(o -> servletPath.equals(o.getMenuHref()))) {
                    return true;
            }
            response.sendRedirect(contextPath + "/"+ Constants.STRING_PAGE_NOT_FOUND);
            return false;
        }
        return true;
    }

}
