package com.wm.v1miaosha.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author weimin
 * @ClassName BloomBean
 * @Description TODO
 * @date 2019/11/28 19:03
 */
@Component
public class BloomBean {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private DefaultRedisScript<List> redisScript;

    @PostConstruct
    public void initBloom(){
        DefaultRedisScript<Object> lockScript = new DefaultRedisScript<>();
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("isex.lua")));
        lockScript.setResultType(Object.class);
        List<String> keyList = new ArrayList<>();
        //bf.insert test capacity  10000 error 0.00001 items  a b c
        keyList.add("mybloom");
        List<Boolean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            keyList.add(i+"");
        }
        Object result = redisTemplate.execute(lockScript, keyList);
        System.out.println(result);
    }

    public boolean isExitOne(List<String> list){
        Integer execute = (Integer) redisTemplate.execute(redisScript, list).get(0);
        return execute>0;
    }
}
