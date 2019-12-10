package com.wm.v1miaosha.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;

/**
 * @author weimin
 * @ClassName CommonConfig
 * @Description TODO
 * @date 2019/11/22 19:01
 */
@Configuration
public class CommonConfig {

    @Bean
    public RateLimiter getRateLimiter(){
        return RateLimiter.create(1);
    }

    @Bean
    public DefaultRedisScript<List> getListRedisScript(){
        DefaultRedisScript<List> lockScript = new DefaultRedisScript<>();
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("isex.lua")));
        lockScript.setResultType(List.class);
        return lockScript;
    }

}
