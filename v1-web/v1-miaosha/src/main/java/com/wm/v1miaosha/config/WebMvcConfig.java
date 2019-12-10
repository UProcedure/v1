package com.wm.v1miaosha.config;

import com.wm.v1miaosha.Interceptor.LimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author weimin
 * @ClassName WebMvcConfig
 * @Description TODO
 * @date 2019/11/22 18:56
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LimitInterceptor limitInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(limitInterceptor).addPathPatterns("/**");
    }
}
