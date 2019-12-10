package com.wm.v1cart.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wm.service.ICartService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author weimin
 * @ClassName CartHandler
 * @Description TODO
 * @date 2019/11/15 15:39
 */
@Component
public class CartHandler {

    @Reference
    private ICartService cartService;

    @RabbitHandler
    @RabbitListener(queues = "cart:queue")
    public void integrate(Map<String,String> map){
        cartService.combine(map.get("noLoginKey"),map.get("loginKey"));
    }
}
