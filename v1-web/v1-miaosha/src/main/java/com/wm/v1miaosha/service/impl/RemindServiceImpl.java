package com.wm.v1miaosha.service.impl;

import cn.hutool.json.JSONUtil;
import com.wm.v1miaosha.entity.MiaoshaProduct;
import com.wm.v1miaosha.mapper.MiaoshaProductMapper;
import com.wm.v1miaosha.pojo.ResultBean;
import com.wm.v1miaosha.service.IRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author weimin
 * @ClassName RemindServiceImpl
 * @Description TODO
 * @date 2019/11/27 18:56
 */
@Service
public class RemindServiceImpl implements IRemindService {

    @Autowired
    private MiaoshaProductMapper miaoshaProductMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public ResultBean add(Long userId, Long miaoshaId) {
        MiaoshaProduct miaoshaProduct = miaoshaProductMapper.selectByPrimaryKey(miaoshaId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(miaoshaProduct.getStartTime());
        calendar.set(Calendar.MINUTE,-10);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String format = simpleDateFormat.format(calendar.getTime());
        System.out.println(format);
        Map<String, String> map = new HashMap<>(2);
        map.put("userId",String.valueOf(userId));
        map.put("miaoshaProduct", JSONUtil.toJsonStr(miaoshaProduct));
        redisTemplate.opsForZSet().add("remind:miaosha",map,Double.valueOf(format));
        System.out.println("添加推送消息成功");
        return new ResultBean(200,"ok");
    }
}
