package com.wm.v1index;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
public class V1IndexApplicationTests {

    @Resource(name = "redisTemplate1")
    private RedisTemplate<String,Object> redisTemplate;


    @Test
    public void contextLoads() {
        redisTemplate.opsForValue().set("k3","v3");
        Object k3 = redisTemplate.opsForValue().get("k3");
        System.out.println(k3);
    }

}
