package com.wm.api;

import com.wm.base.entity.ResultBean;

import java.io.InputStream;

/**
 * @author qq166
 */
public interface IMailService {

    void sendSimpleMail(String to,String subject,String content);
    boolean sendHTMLMail(String to, String subject, String content,boolean retry);
    void sendAttachmentMail(String to, String subject, String content, InputStream inputStream, String fileName);
}
