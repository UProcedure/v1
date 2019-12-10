package com.wm.v1cart.interceptors;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wm.api.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author weimin
 * @ClassName CartInterceptors
 * @Description TODO
 * @date 2019/11/15 17:14
 */
@Component
public class CartInterceptors implements HandlerInterceptor {

    @Reference
    private IUserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

}
