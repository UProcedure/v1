package com.wm.v1mailservice.handler;

import com.wm.api.IMailService;
import com.wm.constant.MQConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author weimin
 * @ClassName MailHandler
 * @Description TODO
 * @date 2019/11/12 15:49
 */
@Component
public class MailHandler {

    @Autowired
    private IMailService mailService;

    @RabbitListener(queues = MQConstant.QUEUE.EMAIL_EXCHANGE_CODE)
    @RabbitHandler
    public void emailCode(Map<String,String> map){
        System.out.println(map);
        mailService.sendHTMLMail(map.get("identification"),"xx商城验证码",map.get("code"),false);
    }
}
