package com.wm.v1cart.config;

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
        return new Queue("cart:queue");
    }

    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange("login-exchange");
    }

    @Bean
    public Binding bindingExchange(Queue getQueue,TopicExchange getTopicExchange){
        return BindingBuilder.bind(getQueue).to(getTopicExchange).with("login");
    }
}
