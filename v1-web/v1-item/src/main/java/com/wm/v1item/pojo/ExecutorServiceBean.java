package com.wm.v1item.pojo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author weimin
 * @ClassName ExecutorServiceBean
 * @Description TODO
 * @date 2019/11/4 18:50
 */
@Configuration
public class ExecutorServiceBean {

    @Bean
    public ExecutorService executorService(){
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
                corePoolSize,corePoolSize*2,
                0L, TimeUnit.SECONDS,new LinkedBlockingQueue(100));
    }
}
