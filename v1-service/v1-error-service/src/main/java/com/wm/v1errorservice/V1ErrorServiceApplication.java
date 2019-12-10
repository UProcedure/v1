package com.wm.v1errorservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.wm.mapper")
public class V1ErrorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(V1ErrorServiceApplication.class, args);
    }

}
