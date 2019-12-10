package com.wm.v1mailservice.service;

import cn.hutool.core.io.IoUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.wm.api.IEmailErrorService;
import com.wm.api.IMailService;
import com.wm.base.entity.ResultBean;
import com.wm.entity.ErrorEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;

/**
 * @author weimin
 * @ClassName MailServiceImpl
 * @Description TODO
 * @date 2019/11/8 19:19
 */
@Service
public class MailServiceImpl implements IMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Reference
    private IEmailErrorService emailErrorService;


    @Value("${mail.fromAddr}")
    private String fromAddr;

    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromAddr);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        mailSender.send(mailMessage);
    }

    @Override
    public boolean sendHTMLMail(String to, String subject, String content,boolean retry){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            //TODO 抛出异常测试
            // throw new MessagingException();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(fromAddr);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(mimeMessage);
            //TODO 非第一次发送邮件成功之后，删除记录
            if(retry){
                int i = emailErrorService.deleteByToEmailType(to,1);
            }
        } catch (MessagingException e) {
            Long id = emailErrorService.addExceptionMessage(to,content,1);
            return false;
        }
        return true;
    }

    @Override
    public void sendAttachmentMail(String to, String subject, String content, InputStream inputStream,String fileName) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(fromAddr);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            helper.addAttachment(fileName, new ByteArrayResource(IoUtil.readBytes(inputStream)));
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
