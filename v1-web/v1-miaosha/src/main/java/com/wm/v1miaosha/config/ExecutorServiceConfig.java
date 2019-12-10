package com.wm.v1miaosha.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author weimin
 */
@Configuration
public class ExecutorServiceConfig {

    @Bean
    public ExecutorService executorService(){
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
                corePoolSize,corePoolSize*2,
                0L, TimeUnit.SECONDS,new LinkedBlockingQueue(100));
    }
}
