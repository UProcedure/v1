package com.wm.v1searchservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author weimin
 */
@SpringBootApplication
@EnableDubbo
@MapperScan("com.wm.mapper")
public class V1SearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(V1SearchServiceApplication.class, args);
    }

}
