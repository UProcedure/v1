package com.wm.v1miaosha;

import com.wm.v1miaosha.service.IMiaoShaProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class V1MiaoshaApplicationTests {

    @Autowired
    private IMiaoShaProductService miaoShaProductService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    public void contextLoads() {
        List<String> list = (List<String>) redisTemplate.opsForValue().get("test");

        if(list==null){//t1 //t2
            String uuid = UUID.randomUUID().toString();
            Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid,1, TimeUnit.MINUTES);
            if(lock){
                System.out.println("查询数据库");
                //t1
                list = new ArrayList<>();
                list.add(new String("222"));
                list.add(new String("22233"));
                redisTemplate.opsForValue().set("test",list);
                if(uuid.equals(redisTemplate.opsForValue().get("lock"))){
                    redisTemplate.delete("lock");
                }
            }
        }
        System.out.println("查询缓存");
    }

    @Test
    public void pool() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8,
                1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));
        threadPoolExecutor.execute(()->{
            for (int i = 0; i < 10; i++) {
                contextLoads();
            }
        });
        Thread.sleep(10000);
    }


    @Test
    public void bloomTest(){
        DefaultRedisScript<List> lockScript = new DefaultRedisScript<>();
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("bloom.lua")));
        lockScript.setResultType(List.class);

        List<String> keyList = new ArrayList<>();
        //bf.insert test capacity  10000 error 0.00001 items  a b c
        keyList.add("mybloom");
        keyList.add("10000");
        keyList.add("0.1");
        for (int i = 0; i < 100; i++) {
            keyList.add(i+"");
        }
        //5.执行脚本
        List<List> result = redisTemplate.execute(lockScript, keyList);
        System.out.println(result.size()+"--->"+result.get(4).get(0));

    }

    @Test
    public void bloomIsTest(){
        //1.创建一个可以执行lua脚本的执行对象
        DefaultRedisScript<Object> lockScript = new DefaultRedisScript<>();
        //2.获取要执行的脚本
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("isex.lua")));
        //3.设置返回类型
        lockScript.setResultType(Object.class);
        //4.封装参数
        List<String> keyList = new ArrayList<>();
        //bf.insert test capacity  10000 error 0.00001 items  a b c
        keyList.add("mybloom");
        //5.执行脚本
        List<Boolean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            keyList.add(i+"");
        }
        Object result = redisTemplate.execute(lockScript, keyList);
        System.out.println(result);
    }

}
