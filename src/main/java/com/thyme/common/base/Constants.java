package com.thyme.common.base;

/**
 * @author thyme
 * @ClassName Constant
 * @Description TODO
 * @Date 2019/12/19 15:48
 */
public class Constants {

    public static final String LOGIN_URL = "/login";

    public static final String LOGOUT_URL = "/logout";

    public static final Integer INT_PAGE_ERROR = 500;

    public static final Integer INT_PAGE_FORBIDDEN = 403;

    public static final String STRING_PAGE_ERROR = "error";//500页面

    public static final String STRING_PAGE_NOT_FOUND = "404";//404页面

    public static final String STRING_PAGE_FORBIDDEN = "403";//403页面

    public static final String REQUEST_MODE_POST = "POST";

    public static final String LOGIN_SUCCESS = "登录成功";

    public static final String LOGIN_FAIL = "登录失败";

    public static final String LOGOUT_SUCCESS = "退出成功";

    public static final String LOGIN_MAX_LIMIT = "登录超出最大限制";

    //登入错误次数限制
    public static final String LOGIN_FAIL_USER = "LOGIN_FAIL_USER";

    public static final String LOGIN_FAIL_MSG = "密码错误次数过多";

    public static final Long LOGIN_FAIL_LIMIT = 1800L;

    /**
     * 五分钟需要之内允许修改密码错误三次
     */
    public static final Long PASSWORD_UPDATE_MINUTE = 300L;

    public static final String PASSWORD_UPDATE = "PASSWORD_UPDATE";

    public static final Integer ACCESS_AUTH_FILTER_ORDER = 10;

    public static final String MENU_ICON_PREFIX = "layui-icon ";


    /**
     * 用于IP定位转换
     */
    public static final String REGION = "内网IP|内网IP";

    public static final String INTRANET_IP = "内网IP";

    public static final String LOCAL_HOST = "127.0.0.1";

    /**
     * 重置密码  123456
     */
    public static final String CZMM = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
}
