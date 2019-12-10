package com.wm.v1miaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@MapperScan("com.wm.v1miaosha.mapper")
@EnableScheduling
public class V1MiaoshaApplication {

    public static void main(String[] args) {
        SpringApplication.run(V1MiaoshaApplication.class, args);
    }

}
