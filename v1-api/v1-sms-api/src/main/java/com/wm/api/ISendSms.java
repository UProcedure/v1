package com.wm.api;

import com.aliyuncs.exceptions.ClientException;
import com.github.qcloudsms.httpclient.HTTPException;

import java.io.IOException;

/**
 * @author weimin
 */
public interface ISendSms {
    /**
     *TODO: 腾讯短信发送
     *@author weimin
     *@date 2019/11/11
     *@param toPhone
     *@param code
     *@return
     */
    public String tencentSendSms(String toPhone,String code) throws HTTPException, IOException;
    /**
     *TODO: 阿里短信发送
     *@author weimin
     *@date 2019/11/11
     *@param toPhone
     *@param code
     *@return
     */
    public String aliSendSms(String toPhone,String code) throws ClientException;

    /**
     *TODO: 秒嘀科技短信发送
     *@author weimin
     *@date 2019/11/11
     *@param toPhone
     *@param code
     *@return
     */
    public String miaoDiSendSms(String toPhone,String code);

}
