package com.wm.v1mailservice.pojo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wm.api.IEmailErrorService;
import com.wm.api.IMailService;
import com.wm.entity.ErrorEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author weimin
 * @ClassName EmailSendExceptionSchedulerTask
 * @Description TODO
 * @date 2019/11/12 19:05
 */
@Component
public class EmailSendExceptionSchedulerTask {
    //TODO 最大重试次数
    private final int MAX_RETRY_NUM = 3;
    @Reference
    private IEmailErrorService emailErrorService;
    @Autowired
    private IMailService mailService;
    @Autowired
    private ExecutorService executorService;

    //@Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "0 1 * * * ?")
    private void run(){
        List<ErrorEmail> list = emailErrorService.getRetryList(MAX_RETRY_NUM);
        System.out.println(Thread.currentThread().getName()+list);
        if(list != null || list.size() != 0){
            for (ErrorEmail errorEmail : list) {
                //TODO 多线程调用
                executorService.submit(new EmailTake(errorEmail));
            }
        }
    }


    private class EmailTake implements Runnable{

        private ErrorEmail errorEmail;

        public EmailTake(ErrorEmail errorEmail){
            this.errorEmail = errorEmail;
        }

        @Override
        public void run() {
            mailService.sendHTMLMail(errorEmail.getToEmail(),
                    "xx商城验证码", errorEmail.getParams(),true);
        }
    }
}
