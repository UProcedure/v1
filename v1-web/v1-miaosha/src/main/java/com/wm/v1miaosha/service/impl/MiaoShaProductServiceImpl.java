package com.wm.v1miaosha.service.impl;

import com.wm.v1miaosha.entity.MiaoshaProduct;
import com.wm.v1miaosha.exception.MiaoshaException;
import com.wm.v1miaosha.mapper.MiaoshaProductMapper;
import com.wm.v1miaosha.pojo.ResultBean;
import com.wm.v1miaosha.service.IMiaoShaProductService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author weimin
 * @ClassName MiaoShaProductServiceImpl
 * @Description TODO
 * @date 2019/11/20 14:27
 */
@Service
public class MiaoShaProductServiceImpl implements IMiaoShaProductService {

    @Autowired
    private MiaoshaProductMapper miaoshaProductMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ExecutorService executorService;

    @Override
    public MiaoshaProduct getById(long id) {
        return miaoshaProductMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<MiaoshaProduct> getCanStartKillProduct() {
        return miaoshaProductMapper.getCanStartKillProduct();
    }

    @Override
    public ResultBean kill(Long miaoshaId, Long userId, String path) {
        StringBuilder pathKey = new StringBuilder("miaosha:").append(userId).append(":").append(miaoshaId);
        String o = (String) redisTemplate.opsForValue().get(pathKey.toString());
        if(o==null || !o.equals(path)){
            throw new MiaoshaException("访问不合法");
        }
        redisTemplate.delete(pathKey.toString());
        StringBuilder infoKey = new StringBuilder("miaosha:info:").append(miaoshaId);
        StringBuilder key = new StringBuilder("miaosha:product:").append(miaoshaId);
        StringBuilder userKey = new StringBuilder("miaosha:user:").append(miaoshaId);
        MiaoshaProduct miaoshaProduct = (MiaoshaProduct) redisTemplate.opsForValue().get(infoKey.toString());
        if("0".equals(miaoshaProduct.getStatus())){
            throw new MiaoshaException("活动未开启");
        }
        if("2".equals(miaoshaProduct.getStatus())){
            throw new MiaoshaException("活动已结束");
        }
        Long add = redisTemplate.opsForSet().add(userKey.toString(), userId);
        if(add==0){
            throw new MiaoshaException("您已参与该活动");
        }
        Long productId = (Long)redisTemplate.opsForList().leftPop(key.toString());
        if(productId==null){
            redisTemplate.opsForSet().remove(userKey.toString(),userId);
            throw new MiaoshaException("您未抢到改商品");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSSS");
        String orderNo = new StringBuilder(dateFormat.format(System.currentTimeMillis()))
                .append(userId).toString();
        Map<String,Object> params = new HashMap<>(4);
        params.put("userId",userId);
        params.put("productId",miaoshaProduct.getProductId());
        params.put("count",1);
        params.put("productPrice",miaoshaProduct.getSalePrice());
        params.put("orderNo",orderNo);
        rabbitTemplate.convertAndSend("order:exchange","order:create",params);
        executorService.submit(() -> {
            miaoshaProductMapper.updateCount(miaoshaProduct.getId());
        });
        return new ResultBean(200,orderNo);
    }

    @Override
    public ResultBean getMiaoshaPath(Long miaoshaId, Long userId) {
        StringBuilder infoKey = new StringBuilder("miaosha:info:").append(miaoshaId);
        MiaoshaProduct miaoshaProduct = (MiaoshaProduct) redisTemplate.opsForValue().get(infoKey.toString());
        if("0".equals(miaoshaProduct.getStatus())){
            throw new MiaoshaException("活动未开启");
        }
        if("2".equals(miaoshaProduct.getStatus())){
            throw new MiaoshaException("活动已结束");
        }
        StringBuilder pathKey = new StringBuilder("miaosha:").append(userId).append(":").append(miaoshaId);
        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(pathKey.toString(),uuid,5, TimeUnit.MINUTES);
        return new ResultBean(200,uuid);
    }

    @Override
    public void test() {
        CorrelationData data = new CorrelationData();
        data.setId("1");
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("a","xxxxx");
        data.setReturnedMessage(new Message(null,messageProperties));
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            Map<String, Object> headers = correlationData.getReturnedMessage().getMessageProperties().getHeaders();
            System.out.println(correlationData.getId()+"--->"+headers.get("a"));
        });
        rabbitTemplate.convertAndSend("","ttl-queue","123456",data);
    }

}
