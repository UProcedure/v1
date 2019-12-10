package com.wm.v1miaosha.Interceptor;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author weimin
 * @ClassName LimitInterceptor
 * @Description TODO
 * @date 2019/11/22 18:55
 */
@Component
public class LimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimiter rateLimiter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean b = rateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS);
        if (!b) {
            System.out.println("限流开启中");
            return false;
        }
        System.out.println("模拟业务进行");
        return true;
    }
}
