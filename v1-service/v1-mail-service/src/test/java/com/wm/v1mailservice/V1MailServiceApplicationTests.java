package com.wm.v1mailservice;

import com.wm.api.IMailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootTest
public class V1MailServiceApplicationTests {

    @Autowired
    private IMailService mailService;


    @Test
    public void contextLoads() throws IOException {
        mailService.sendSimpleMail("2752950990@qq.com","吴应强","哈哈哈哈哈");

        /*String s = "http://123.207.253.16:88/group1/M00/00/00/rBAAB128OEqAHA0mAABsFtOtc9U812.jpg";
        URL url = new URL(s);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = connection.getInputStream();
        mailService.sendAttachmentMail("2624327119@qq.com","吴应强","哈哈哈哈哈",
                inputStream,"haochi.jpg");*/
    }
}
