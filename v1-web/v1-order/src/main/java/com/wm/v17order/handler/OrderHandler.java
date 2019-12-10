package com.wm.v17order.handler;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author weimin
 * @ClassName OrderHandler
 * @Description TODO
 * @date 2019/11/21 18:06
 */
@Component
public class OrderHandler {


    @RabbitListener(queues = "order:miaosha")
    @RabbitHandler
    public void createOrder(Map<String,Object> map, Message message, Channel channel){
        System.out.println(map);
        try {
            Thread.sleep(6000);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (InterruptedException | IOException e) {

        }
    }

    @RabbitListener(queues = "dex-queue")
    @RabbitHandler
    public void cancerOrder(String msg,Message message, Channel channel){
        System.out.println(msg);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
