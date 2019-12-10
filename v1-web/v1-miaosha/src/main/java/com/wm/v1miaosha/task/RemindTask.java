package com.wm.v1miaosha.task;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author weimin
 * @ClassName RemindTask
 * @Description TODO
 * @date 2019/11/27 18:52
 */
@Component
public class RemindTask {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0 12 * * * *")
    public void remind(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Long format = Long.valueOf(simpleDateFormat.format(new Date()));
        Set<Object> range = redisTemplate.opsForZSet().rangeByScore("remind:miaosha",
                Double.valueOf(201911211650L), Double.valueOf(201911211650L));
        if(range!=null && !range.isEmpty()){
            rabbitTemplate.convertAndSend("remind-exchange","miaosha:remind",range);
            System.out.println("发送消息成功");
        }
    }
}
