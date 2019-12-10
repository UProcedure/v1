package com.wm.v1smsservice;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.github.qcloudsms.httpclient.HTTPException;
import com.wm.api.ISendSms;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class V1SmsServiceApplicationTests {

    @Reference
    private ISendSms sendSms;

    @Test
    public void contextLoads() throws HTTPException, IOException, ClientException {
        String toPhone = "17770724957";
        String code = "123456";
        System.out.println(sendSms.tencentSendSms(toPhone, code));
        System.out.println("==================================");
        System.out.println(sendSms.aliSendSms(toPhone, code));

    }

}
