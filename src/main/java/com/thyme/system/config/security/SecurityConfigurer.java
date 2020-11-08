package com.thyme.system.config.security;

import com.thyme.common.base.Constants;
import com.thyme.system.config.filter.ValidateCodeFilter;
import com.thyme.system.config.security.handler.AuthenticationFailureHandler;
import com.thyme.system.config.security.handler.AuthenticationSuccessHandler;
import com.thyme.system.config.security.handler.CustomLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author thyme
 * @ClassName SecurityConfigurer
 * @Description TODO
 * @Date 2019/12/11 10:47
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    /**
     * 最大登录数(默认1)
     */
    @Value("${security.max-session:1}")
    private Integer maxSession;

    /**
     * 超出最大登录数，是否阻止登录（默认不阻止）
     */
    @Value("${security.prevents-login:false}")
    private Boolean preventsLogin;

    //设置记住我cookie过期时间（秒）
    @Value("${security.rememberExpireTime:3600}")
    private int tokenValiditySeconds;

    private final UserDetailServiceImpl userDetailService;

    private final CustomAuthenticationProvider customAuthenticationProvider;

    private final  ValidateCodeFilter validateCodeFilter;

    private final CustomInvalidSessionStrategy customInvalidSessionStrategy;

    private final CustomExpiredSessionStrategy customExpiredSessionStrategy;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().disable();

        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                //放行所有的 css和js文件
                .antMatchers("/static/**","/favicon.ico","/actuator/**","/code","/invalid_session","/expired","/logout","/403","/uploadimg/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
            .rememberMe()
                .rememberMeParameter("rememberMe")
                .tokenRepository(persistentTokenRepository()) // 配置 token 持久化仓库
                .tokenValiditySeconds(tokenValiditySeconds) // remember 过期时间，单为秒
                .userDetailsService(userDetailService)// 处理自动登录逻辑
                .and()
            .formLogin()
                .loginProcessingUrl(Constants.LOGIN_URL)
                .loginPage(Constants.LOGIN_URL)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
                .and()
                .csrf().disable()
                .cors()
                .and()
            .logout()
                .logoutUrl(Constants.LOGOUT_URL)
                .logoutSuccessUrl(Constants.LOGIN_URL)
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .invalidateHttpSession(true)//清理当前session
                .deleteCookies("JSESSIONID","remember-me")//清理指定cookies
                .and()
            .sessionManagement()
                    //.invalidSessionUrl("/invalid_session")
                    //失效处理
                    .invalidSessionStrategy(customInvalidSessionStrategy)
                    //同一账号同时允许多个设备在线
                    .maximumSessions(maxSession)
                    //新用户挤走前用户
                    .maxSessionsPreventsLogin(preventsLogin)
                    .expiredUrl(Constants.LOGIN_URL)
                    //超时处理
                    .expiredSessionStrategy(customExpiredSessionStrategy)
                    .sessionRegistry(sessionRegistry);
    }

    /**
     * 校验用户信息
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
//        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    @Bean
    public SessionRegistry getSessionRegistry(){
        return new SessionRegistryImpl();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.setAllowedMethods(Arrays.asList("GET","POST"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return new CorsFilter(source);
    }
}
