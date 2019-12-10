package com.wm.v1msg.ws;

import cn.hutool.json.JSONUtil;
import com.wm.v1msg.pojo.Message;
import com.wm.v1msg.util.ChannelUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

/**
 * @author weimin
 * @ClassName WSHandler
 * @Description TODO
 * @date 2019/11/27 11:30
 */
@Component
@ChannelHandler.Sharable
public class WSHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //ReadTimeoutException

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        System.out.println("接收到的消息:"+text);
        Message message = JSONUtil.toBean(text, Message.class);
        if("1".equals(message.getMessageType())){
            ChannelUtils.add((String) message.getData(),channelHandlerContext.channel());
            System.out.println("user:"+message.getData()+"-->"+channelHandlerContext.channel().remoteAddress()+"已经连接");
            System.out.println(ChannelUtils.getChannel((String) message.getData()));
        }
    }


}
