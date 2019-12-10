package com.wm.v1smsservice.config;

import com.wm.constant.MQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
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
    public Queue getQueue(){
        return new Queue(MQConstant.QUEUE.SMS_EXCHANGE_CODE);
    }

    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange(MQConstant.EXCHANGE.V1_SMS_EXCHANGE);
    }

    @Bean
    public Binding bindingExchange(Queue getQueue, TopicExchange getTopicExchange){
        return BindingBuilder.bind(getQueue).to(getTopicExchange).with("sms:code");
    }
}
