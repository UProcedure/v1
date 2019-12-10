package com.wm.v17order.interceptors;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wm.api.IUserService;
import com.wm.base.entity.ResultBean;
import com.wm.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author weimin
 * @ClassName OrderInterceptors
 * @Description TODO
 * @date 2019/11/18 19:46
 */
@Component
public class OrderInterceptors implements HandlerInterceptor {

    @Reference
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if("user_token".equals(cookie.getName())){
                ResultBean<User> userResultBean = userService.checkIsLogin(cookie.getValue());
                if("200".equals(userResultBean.getStatusCode())){
                    return true;
                }
            }
        }
        System.out.println(request.getRequestURL());
        response.setHeader("Referer",request.getRequestURL().toString());
        response.sendRedirect("http://localhost:9095/");
        return false;
    }
}
