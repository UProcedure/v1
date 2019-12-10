package com.wm.v1msg.handler;

import com.wm.v1msg.util.ChannelUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author weimin
 * @ClassName RemindHandler
 * @Description TODO
 * @date 2019/11/27 19:32
 */
@Component
public class RemindHandler {

    @RabbitListener(queues = "miaosha:remind")
    @RabbitHandler
    public void remind(Set<Object> set){
        set.forEach((v)->{
            Map<String,String> map = (Map<String, String>) v;
            Channel channel = ChannelUtils.getChannel(map.get("userId"));
            if(channel!=null){
                channel.writeAndFlush(new TextWebSocketFrame(map.get("miaoshaProduct")));
            }
        });
    }

}
