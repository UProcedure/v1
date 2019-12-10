package com.wm.v1smsservice.handler;

import com.github.qcloudsms.httpclient.HTTPException;
import com.wm.api.ISendSms;
import com.wm.constant.MQConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author weimin
 * @ClassName SmsHandler
 * @Description TODO
 * @date 2019/11/12 11:59
 */
@Component
public class SmsHandler {

    @Autowired
    private ISendSms sendSms;


    @RabbitListener(queues = MQConstant.QUEUE.SMS_EXCHANGE_CODE)
    @RabbitHandler
    public void codeSms(Map<String,String> map){
        try {
            String s = sendSms.tencentSendSms(map.get("identification"), map.get("code"));
            System.out.println(s);
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
