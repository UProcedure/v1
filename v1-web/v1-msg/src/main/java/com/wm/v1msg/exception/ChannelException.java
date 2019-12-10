package com.wm.v1msg.exception;

import io.netty.handler.timeout.ReadTimeoutException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author weimin
 * @ClassName ChannelException
 * @Description TODO
 * @date 2019/11/27 17:29
 */
@ControllerAdvice
public class ChannelException {

    @ExceptionHandler(ReadTimeoutException.class)
    public void readTimeoutException(ReadTimeoutException e){
        System.out.println("异常处理"+e.getMessage());
    }
}
