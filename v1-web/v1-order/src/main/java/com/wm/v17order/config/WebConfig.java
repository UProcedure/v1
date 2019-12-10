package com.wm.v17order.config;

import com.wm.v17order.interceptors.OrderInterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author weimin
 * @ClassName WebConfig
 * @Description TODO
 * @date 2019/11/15 17:12
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private OrderInterceptors orderInterceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(orderInterceptors).addPathPatterns("/order/**");
    }
}
