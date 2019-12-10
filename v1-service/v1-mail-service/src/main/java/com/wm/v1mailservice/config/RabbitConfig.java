package com.wm.v1mailservice.config;

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
        return new Queue(MQConstant.QUEUE.EMAIL_EXCHANGE_CODE);
    }

    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange(MQConstant.EXCHANGE.V1_EMAIL_EXCHANGE);
    }

    @Bean
    public Binding bindingExchange(){
        return BindingBuilder.bind(getQueue()).to(getTopicExchange()).with("email:code");
    }
}
