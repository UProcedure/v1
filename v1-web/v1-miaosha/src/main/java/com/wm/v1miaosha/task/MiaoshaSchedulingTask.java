package com.wm.v1miaosha.task;

import com.wm.v1miaosha.entity.MiaoshaProduct;
import com.wm.v1miaosha.mapper.MiaoshaProductMapper;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author weimin
 * @ClassName MiaoshaSchedulingTask
 * @Description TODO
 * @date 2019/11/20 17:22
 */
@Component
public class MiaoshaSchedulingTask {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private MiaoshaProductMapper miaoshaProductMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0/5 * * * * ?")
    public void killStart(){
        List<MiaoshaProduct> list = miaoshaProductMapper.getCanStartKillProduct();
        if(list != null && !list.isEmpty()){
            for (MiaoshaProduct miaoshaProduct : list) {
                StringBuilder key = new StringBuilder("miaosha:product:")
                                    .append(miaoshaProduct.getId());
                StringBuilder infoKey = new StringBuilder("miaosha:info:")
                        .append(miaoshaProduct.getId());
                redisTemplate.executePipelined(new SessionCallback<Object>() {
                    @Override
                    public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                        for (Integer i = 0; i < miaoshaProduct.getCount(); i++) {
                            redisTemplate.opsForList().leftPush(key.toString(),miaoshaProduct.getProductId());
                        }
                        return null;
                    }
                });
                miaoshaProduct.setStatus("1");
                miaoshaProductMapper.updateByPrimaryKey(miaoshaProduct);
                redisTemplate.opsForValue().set(infoKey.toString(),miaoshaProduct);
            }
            System.out.println("redis初始化。。。。。。");
        }
    }


    @Scheduled(cron = "0/5 * * * * ?")
    public void killStop(){
        List<MiaoshaProduct> list = miaoshaProductMapper.getStopKillProduct();
        if(list != null && !list.isEmpty()){
            for (MiaoshaProduct miaoshaProduct : list) {
                StringBuilder key = new StringBuilder("miaosha:product:")
                        .append(miaoshaProduct.getId());
                StringBuilder infoKey = new StringBuilder("miaosha:info:")
                        .append(miaoshaProduct.getId());
                List<String> list1 = new ArrayList<>(2);
                list1.add(key.toString());
                list1.add(infoKey.toString());
                redisTemplate.delete(list1);
                miaoshaProduct.setStatus("2");
                miaoshaProductMapper.updateByPrimaryKey(miaoshaProduct);
                if(miaoshaProduct.getCount()!=0){
                    System.out.println("异步更新库存："+miaoshaProduct.getCount());
                }
            }
            System.out.println("redis清理。。。。。。");
        }
    }
}
