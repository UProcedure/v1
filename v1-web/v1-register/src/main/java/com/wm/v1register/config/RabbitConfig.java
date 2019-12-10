package com.wm.v1register.config;

import com.wm.constant.MQConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weimin
 * @ClassName RabbitConfig
 * @Description TODO
 * @date 2019/11/5 18:36
 */
@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange(MQConstant.EXCHANGE.V1_SMS_EXCHANGE);
    }

    @Bean
    public TopicExchange getEmailTopicExchange(){
        return new TopicExchange(MQConstant.EXCHANGE.V1_EMAIL_EXCHANGE);
    }
}
