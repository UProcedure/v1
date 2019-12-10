package com.wm.v1mailservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDubbo
@EnableScheduling
public class V1MailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(V1MailServiceApplication.class, args);
    }

}
