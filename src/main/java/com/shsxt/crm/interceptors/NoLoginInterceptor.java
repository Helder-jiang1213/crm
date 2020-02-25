package com.shsxt.crm.interceptors;

import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
           获取cookies，解析用户Id
        *  如果用户Id存在，并且数据库在对应用胡记录放行 否则进行拦截 重定向跳转到登录
        *
        *
        * */

        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        if (userId==0 || null==userService.selectByPrimaryKey(userId)){
            throw new NoLoginException();
        }

        return true;
    }
}
