package com.wm.v1msg.ws;

import com.wm.v1msg.util.ChannelUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author weimin
 * @ClassName WSReadTimeoutHandler
 * @Description TODO
 * @date 2019/11/27 17:44
 */
public class WSReadTimeoutHandler extends ReadTimeoutHandler {

    public WSReadTimeoutHandler(long timeout, TimeUnit unit) {
        super(timeout, unit);
    }

    @Override
    protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
        super.readTimedOut(ctx);
        System.out.println("超时处理中。。。。");
        ChannelUtils.remove(ctx.channel());
    }

}
