package com.wm.v1smsservice.pojo;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.github.qcloudsms.SmsSingleSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weimin
 * @ClassName SendSmsBean
 * @Description TODO
 * @date 2019/11/11 19:02
 */
@Configuration
public class SendSmsBean {
    /**TODO:腾讯短信服务公共参数*/
    private final int APP_ID = 1400284981;
    private final String APP_KEY = "8807a540404ca2d1a2ef2a2ae7ef0d27";
    /**TODO:阿里云短信服务公共参数*/
    private final String ACCESSKEY_ID = "LTAI4FqH2v37r4b8NfRkeih1";
    private final String ACCESSKEY_SECRET= "bnFZBSuaNWBn4dkSWkgayCBPSIzR0a";
    private final String REGION_ID= "cn-qingdao";

    @Bean
    public SmsSingleSender getSmsSingleSender(){
        return new SmsSingleSender(APP_ID, APP_KEY);
    }

    @Bean
    public IAcsClient getIAcsClient(){
        return new DefaultAcsClient(DefaultProfile.getProfile(REGION_ID,ACCESSKEY_ID,ACCESSKEY_SECRET));
    }
}
