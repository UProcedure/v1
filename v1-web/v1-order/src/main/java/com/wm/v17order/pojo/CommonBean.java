package com.wm.v17order.pojo;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weimin
 * @ClassName CommonBean
 * @Description TODO
 * @date 2019/11/18 19:37
 */
@Configuration
public class CommonBean {
    @Value("${pay.Alipay_public_key}")
    private String ALIPAY_PUBLIC_KEY;
    @Value("${pay.Merchant_private_key}")
    private String MERCHANT_PRIVATE_KEY;
    @Value("${pay.app_id}")
    private String APP_ID;
    @Value("${pay.server_url}")
    private String server_url;

    @Bean
    public AlipayClient getAlipayClient(){
        return new DefaultAlipayClient(server_url,APP_ID,
                MERCHANT_PRIVATE_KEY,"json", "UTF-8",
                ALIPAY_PUBLIC_KEY,"RSA2");
    }

}
