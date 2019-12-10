package com.wm.v1mailservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.*;

/**
 * @author weimin
 * @ClassName SchedulerConfig
 * @Description TODO
 * @date 2019/11/12 19:06
 */
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(getTaskExecutor());
    }

    @Bean
    public Executor getTaskExecutor(){
        return Executors.newScheduledThreadPool(100);
    }
}
