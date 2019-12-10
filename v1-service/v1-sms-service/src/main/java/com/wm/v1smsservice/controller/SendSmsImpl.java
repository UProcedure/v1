package com.wm.v1smsservice.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.wm.api.ISendSms;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author weimin
 * @ClassName SendSmsImpl
 * @Description TODO:短信发送
 * @date 2019/11/11 18:56
 */
@Service
public class SendSmsImpl implements ISendSms {
    /**TODO:腾讯短信服务*/
    @Autowired
    private SmsSingleSender sender;
    private final int TEMPLATE_ID = 468885;
    private final String SMS_SIGN = "沉默的彼岸花";
    /**TODO:阿里云短信服务*/
    @Autowired
    private IAcsClient client;
    private final String SIGN_NAME = "彼岸花网站";
    private final String TEMPLATE_CODE = "SMS_177241071";
    /**TODO:秒嘀科技短信服务*/
    private final String URL = "https://openapi.miaodiyun.com/distributor/sendSMS";
    private final String ACCOUNT_SID = "81db690cfa66e81894875ec43f885ac4";
    private final String AUTH_TOKEN = "904f36cc022b193053653045d4eb98c9";



    @Override
    public String tencentSendSms(String toPhone, String code) throws HTTPException, IOException {
        String[] params = {code,"2"};
        SmsSingleSenderResult result = sender.sendWithParam("86", toPhone,
                TEMPLATE_ID, params, SMS_SIGN, "", "");
        return result.toString();
    }

    @Override
    public String aliSendSms(String toPhone, String code) throws ClientException {
        CommonRequest request = new CommonRequest();
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", toPhone);
        request.putQueryParameter("SignName", SIGN_NAME);
        request.putQueryParameter("TemplateCode", TEMPLATE_CODE);
        request.putQueryParameter("TemplateParam", "{\"code\":"+code+"}");
        CommonResponse commonResponse = client.getCommonResponse(request);
        String data = commonResponse.getData();
        System.out.println("date="+data);
        String sData = data.replaceAll("'\'", "");
        return sData;
    }

    @Override
    public String miaoDiSendSms(String toPhone, String code) {
        String timestamp = Convert.toStr(System.currentTimeMillis());
        String sig = DigestUtil.md5Hex(ACCOUNT_SID + AUTH_TOKEN + timestamp);
        StringBuilder builder = new StringBuilder();
        builder.append("accountSid="+ACCOUNT_SID).append("&to="+toPhone).append("&smsContent="+code);
        builder.append("&timestamp="+timestamp).append("&sig="+sig);
        String result = HttpUtil.post(URL,builder.toString());
        return result;
    }
}
