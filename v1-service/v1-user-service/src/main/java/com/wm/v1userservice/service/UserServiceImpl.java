package com.wm.v1userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.wm.api.IUserService;
import com.wm.base.BaseServiceImpl;
import com.wm.base.IBaseDao;
import com.wm.base.entity.ResultBean;
import com.wm.entity.User;
import com.wm.mapper.UserMapper;
import com.wm.v1userservice.util.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author weimin
 * @ClassName UserServiceImpl
 * @Description TODO
 * @date 2019/11/11 17:47
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Resource(name = "redisTemplate1")
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public ResultBean<Boolean> checkUserNameIsExist(String username) {
        User user = userMapper.getUserByUserName(username);
        return new ResultBean<>(1,user!=null);
    }

    @Override
    public ResultBean<Boolean> checkPhoneIsExist(String phone) {
        User user = userMapper.getUserByPhone(phone);
        return new ResultBean<>(1,user!=null);
    }

    @Override
    public ResultBean<Boolean> checkEmailIsExist(String email) {
        User user = userMapper.getUserByEmail(email);
        return new ResultBean<>(1,user!=null);
    }

    @Override
    public int activating(String email) {
        return userMapper.activating(email);
    }

    @Override
    public ResultBean<String> loginAuthentication(String username, String password) {
        User user = userMapper.loginAuthentication(username);
        if (user != null){
            boolean matches = encoder.matches(password, user.getPassword());
            if(!matches){
                return new ResultBean<>(500,"密码错误");
            }
            JwtTokenUtils jwtTokenUtils =
                    new JwtTokenUtils(user.getId().toString(),user.getUsername(),30*60*1000L,"user_token");
            return new ResultBean<>(200,jwtTokenUtils.createJwtToken());
        }
        return new ResultBean<>(404,"不存在");
    }

    @Override
    public ResultBean<Boolean> logout(String uuid) {
        Boolean delete = redisTemplate.delete("user:token:"+uuid);
        return new ResultBean<>(200,delete);
    }

    @Override
    public ResultBean<User> checkIsLogin(String token) {
        try {
            if(token!=null){
                JwtTokenUtils jwtTokenUtils = new JwtTokenUtils("user_token");
                Claims body = jwtTokenUtils.getBody(token);
                User user = new User();
                user.setId(Long.valueOf(body.getId()));
                user.setUsername(body.getSubject());
                return new ResultBean<>(200, user);
            }
        }catch (RuntimeException e){
            return new ResultBean<>(404, null);
        }
        return new ResultBean<>(404, null);
    }

    @Override
    public IBaseDao<User> getBaseDao() {
        return userMapper;
    }
}
