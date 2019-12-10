package com.wm.constant;



/**
 * @author weimin
 */
public interface MQConstant {

    public static class EXCHANGE{
        public static final String V1_PRODUCT_EXCHANGE = "v1-product-exchange";
        public static final String V1_SMS_EXCHANGE = "v1-sms-exchange";
        public static final String V1_EMAIL_EXCHANGE = "v1-email-exchange";
    }

    public static class QUEUE{
        public static final String  PRODUCT_EXCHANGE_QUEUE = "product-exchange-queue";
        public static final String  PRODUCT_EXCHANGE_ITEM = "product-exchange-item";
        public static final String  SMS_EXCHANGE_CODE = "sms-exchange-code";
        public static final String  EMAIL_EXCHANGE_CODE = "email-exchange-code";
    }
}
