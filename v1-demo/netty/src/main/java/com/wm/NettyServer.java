package com.wm;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author weimin
 * @ClassName NettyServer
 * @Description TODO
 * @date 2019/11/26 21:36
 */
public class NettyServer {
    public static void main(String[]args)throws InterruptedException{
        //1.定义主线程组，处理客户端的连接请求
        EventLoopGroup bossGroup= new NioEventLoopGroup();
        //2.定义从线程组，处理由主线程交过来的任务，完成具体的IO操作
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        //3.创建Netty服务启动类对象
        ServerBootstrap serverBootstrap= new ServerBootstrap();
        //4.设置主从线程组
        serverBootstrap.group(bossGroup,workerGroup)
                        //5.设置NIO的双向通道
                        .channel(NioServerSocketChannel.class)
                        //6.设置子处理器，用于处理workGroup的任务
                        .childHandler(new ServerInitializer());
        //7.设置8888端口号，并启动服务,将其变为同步操作
        serverBootstrap.bind(8888).sync();
        System.out.println("start on 8888");

    }

}
