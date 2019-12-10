package com.wm.v1register.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.wm.api.IMailService;
import com.wm.base.entity.ResultBean;
import com.wm.v1register.utils.SendSms;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author weimin
 * @ClassName RegisterController
 * @Description TODO
 * @date 2019/11/8 21:26
 */
@Controller
@Slf4j
public class RegisterController {
    @Reference
    private IMailService mailService;
    @Resource(name = "redisTemplate1")
    private RedisTemplate<String,Object> redisTemplate;
    //秒嘀科技短信验证密钥
    /*private final String ACCOUNT_SID = "81db690cfa66e81894875ec43f885ac4";
    private final String AUTH_TOKEN = "904f36cc022b193053653045d4eb98c9";*/
    /**TODO:腾讯短信服务公共参数*/
    private final int APP_ID = 1400284981;
    private final String APP_KEY = "8807a540404ca2d1a2ef2a2ae7ef0d27";
    private final int TEMPLATE_ID = 468885;
    private final String SMS_SIGN = "沉默的彼岸花";

    @RequestMapping("register")
    public String register(){
        return "register";
    }

    @RequestMapping("toEmail")
    @ResponseBody
    public ResultBean<String> toEmail(String email){
        String code = RandomUtil.randomString(5);
        mailService.sendSimpleMail(email,"xxx商城注册验证", code);
        redisTemplate.opsForValue().set(email,code,4, TimeUnit.MINUTES);
        return new ResultBean<>(200,code);
    }

/*
    TODO:秒嘀科技验证码短信服务
    @RequestMapping("toPhone")
    @ResponseBody
    public ResultBean<String> toPhone(String to){
        String code = RandomUtil.randomString(5);
        String url = "https://openapi.miaodiyun.com/distributor/sendSMS";
        String timestamp = Convert.toStr(System.currentTimeMillis());
        String sig = DigestUtil.md5Hex(ACCOUNT_SID + AUTH_TOKEN + timestamp);
        StringBuilder builder = new StringBuilder();
        builder.append("accountSid="+ACCOUNT_SID).append("&to="+to).append("&smsContent="+code);
        builder.append("&timestamp="+timestamp).append("&sig="+sig);
        String result = HttpUtil.post(url,builder.toString());
        System.out.println(result);
        return new ResultBean<>(200,result);
    }*/


/*
    TODO:阿里云验证码短信服务
    @RequestMapping("toPhone")
    @ResponseBody
    public ResultBean<String> toPhone(String to){
        String code = RandomUtil.randomNumbers(6);
        try {
            HttpUtil.get(new SendSms().getHttpAddr(to,code));
            redisTemplate.opsForValue().set(to,code,2, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultBean<>(500,"");
        }
        return new ResultBean<>(200,"");
    }*/

    /**TODO:腾讯云短信服务*/
    @RequestMapping("toPhone")
    @ResponseBody
    public ResultBean<String> toPhone(String to){
        String code = RandomUtil.randomNumbers(6);
        String[] params = {code,"2"};
        try {
            SmsSingleSender sender = new SmsSingleSender(APP_ID, APP_KEY);
            SmsSingleSenderResult result = sender.sendWithParam("86", to,
                    TEMPLATE_ID, params, SMS_SIGN, "", "");
            redisTemplate.opsForValue().set(to,code,2, TimeUnit.MINUTES);
        } catch (HTTPException | IOException e) {
            log.error(e.getMessage());
            return new ResultBean<>(500,"");
        }
        return new ResultBean<>(200,"");
    }

}
