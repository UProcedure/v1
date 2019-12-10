package com.wm.v1register.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.alibaba.dubbo.config.annotation.Reference;
import com.wm.api.IMailService;
import com.wm.api.ISendSms;
import com.wm.api.IUserService;
import com.wm.base.entity.ResultBean;
import com.wm.constant.MQConstant;
import com.wm.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author weimin
 * @ClassName UserController
 * @Description TODO
 * @date 2019/11/11 18:01
 */
@Slf4j
@Controller
@Component
@RequestMapping("user")
public class UserController {
    private final String PHONE = "phone";
    private final String EMAIL = "email";
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Reference
    private IMailService mailService;
    @Resource(name = "redisTemplate1")
    private RedisTemplate<String,Object> redisTemplate;
    @Reference
    private IUserService userService;
    @Reference
    private ISendSms sendSms;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("checkUserNameIsExist/{username}")
    @ResponseBody
    public ResultBean<Boolean> checkUserNameIsExist(@PathVariable String username){
        return userService.checkUserNameIsExist(username);
    }

    @GetMapping("checkPhoneIsExist/{phone}")
    @ResponseBody
    public ResultBean<Boolean> checkPhoneIsExist(@PathVariable String phone){
        return userService.checkPhoneIsExist(phone);
    }

    @GetMapping("checkEmailIsExist/{email}")
    @ResponseBody
    public ResultBean<Boolean> checkEmailIsExist(@PathVariable String email){
        return userService.checkEmailIsExist(email);
    }


    @RequestMapping("generateCode/{identification}")
    @ResponseBody
    public ResultBean<String> generateCode(@PathVariable String identification,String params){
        String code = RandomUtil.randomNumbers(6);
        System.out.println("code="+code);
        Map<String,String> map = new HashMap<>(2);
        map.put("identification",params);
        map.put("code",code);
        if(PHONE.equals(identification)){
            rabbitTemplate.convertAndSend(MQConstant.EXCHANGE.V1_SMS_EXCHANGE,
                    "sms:code",map);
        }
        if(EMAIL.equals(identification)){
            rabbitTemplate.convertAndSend(MQConstant.EXCHANGE.V1_EMAIL_EXCHANGE,
                    "email:code",map);
        }
        redisTemplate.opsForValue().set(params,code,2, TimeUnit.MINUTES);
        return new ResultBean<>(200,"生成验证码成功");
    }


    @GetMapping("activating")
    @ResponseBody
    public String activating(String token){
        String email = (String) redisTemplate.opsForValue().get(token);
        if(email == null || "".equals(email)){
            return "激活链接已过期";
        }
        int a = userService.activating(email);
        redisTemplate.delete(token);
        return "ok";
    }

    @PostMapping("register")
    @ResponseBody
    public ResultBean<String> register(User user, String code, String identification){
        System.out.println(user);
        System.out.println(code);
        System.out.println(identification);
        if(PHONE.equals(identification)){
            if (code.equals((String) redisTemplate.opsForValue().get(user.getPhone()))){
                return new ResultBean<>(500,"手机验证码错误");
            }
            String password = user.getPassword();
            user.setPassword(encoder.encode(password));
            user.setFlag((byte) 1);
            userService.insertSelective(user);
        }
        if(EMAIL.equals(identification)){
            String codes = (String) redisTemplate.opsForValue().get(user.getEmail());
            if (!code.equals(codes)){
                return new ResultBean<>(500,"邮箱验证码错误");
            }
            String password = user.getPassword();
            user.setPassword(encoder.encode(password));
            user.setFlag((byte) 0);
            user.setPhone(null);
            int i = userService.insertSelective(user);
            if(i==0){
                return new ResultBean<>(500,"邮箱注册失败");
            }
            String token = DigestUtil.md5Hex(user.getEmail());
            //String token = "";
            redisTemplate.opsForValue().set(token,user.getEmail(),15,TimeUnit.MINUTES);
            StringBuilder builder =
                    new StringBuilder("<a href='http://10.36.133.76:9094/user/activating?token=").append(token).append("'>http://10.36.133.76:9094/user/activating?token=");
            builder.append(token);
            builder.append("</a>");
            Map<String,String> map = new HashMap<>(2);
            map.put("identification",user.getEmail());
            map.put("code",builder.toString());
            rabbitTemplate.convertAndSend(MQConstant.EXCHANGE.V1_EMAIL_EXCHANGE,
                    "email:code",map);
        }
        return new ResultBean<>(200,"注册成功");
    }
}
