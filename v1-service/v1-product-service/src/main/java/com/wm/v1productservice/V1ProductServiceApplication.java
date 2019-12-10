package com.wm.v1productservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author qq166
 */
@SpringBootApplication
@EnableDubbo
@MapperScan("com.wm.mapper")
public class V1ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(V1ProductServiceApplication.class, args);
    }

}
