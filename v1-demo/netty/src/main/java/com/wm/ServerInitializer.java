package com.wm;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

import javax.swing.text.ChangedCharSetException;

/**
 * @author weimin
 * @ClassName ServerInitializer
 * @Description TODO
 * @date 2019/11/26 21:53
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //1.通过SocketChannel获取对应的管道对象
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 2.给管道添加Handler
        // HttpServerCodec作用是做编解码
        // 当请求发送到服务端，服务端需要做解码，响应信息到客户端需要做编码
        // netty接收的是二进制数据
        pipeline.addLast(new HttpServerCodec());
        // 3.继续给管道添加Handler
        pipeline.addLast(new HelloHandler());
    }
}
