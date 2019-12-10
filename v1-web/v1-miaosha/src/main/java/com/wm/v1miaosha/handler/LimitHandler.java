package com.wm.v1miaosha.handler;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author weimin
 * @ClassName LimitHandler
 * @Description TODO
 * @date 2019/11/22 19:27
 */
@Component
public class LimitHandler {

    @Autowired
    private RateLimiter rateLimiter;

    @RabbitListener(queues = "miaosha:limit")
    @RabbitHandler
    public void update(Integer count){
        rateLimiter.setRate(count);
    }


}
