package com.wm;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author weimin
 * @ClassName HelloHandler
 * @Description TODO
 * @date 2019/11/26 21:45
 */
public class HelloHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {

        //1.获取channel
        Channel channel = channelHandlerContext.channel();

        //2.如果是一个Http请求信息
        if (httpObject instanceof HttpRequest){

            //3.输出客户端的地址信息
            System.out.println(channel.remoteAddress());

            //4.定义给客户端的响应信息
            ByteBuf content =
                    Unpooled.copiedBuffer("Hello,this is NettyServer", CharsetUtil.UTF_8);

            //5.构建一个响应对象
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,content);

            //6.设置响应的数据类型和长度
             response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
             response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            //7.将响应信息刷到客户端
            channelHandlerContext.writeAndFlush(response);
        }
    }
}
