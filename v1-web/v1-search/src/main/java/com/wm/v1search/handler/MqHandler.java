package com.wm.v1search.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wm.api.ISearchService;
import com.wm.constant.MQConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author weimin
 * @ClassName MqHandler
 * @Description TODO
 * @date 2019/11/7 11:34
 */
@Component
public class MqHandler {

    @Reference
    private ISearchService searchService;
    @RabbitHandler
    @RabbitListener(queues = MQConstant.QUEUE.PRODUCT_EXCHANGE_QUEUE)
    public void add(Long id){
       searchService.synById(id);
    }
}
