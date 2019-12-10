package com.wm.v1login.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wm.api.IUserService;
import com.wm.base.entity.ResultBean;
import com.wm.entity.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author weimin
 * @ClassName LoginController
 * @Description TODO
 * @date 2019/11/13 13:09
 */
@Controller
public class LoginController {

    @Reference
    private IUserService userService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("loginAuthentication")
    public String loginAuthentication(String username, String password,
                                      HttpServletRequest request,HttpServletResponse response,
                                      @CookieValue(name = "cart_token",required = false) String token){
        ResultBean<String> result = userService.loginAuthentication(username,password);
        if(result.getStatusCode()==200){
            Cookie cookie = new Cookie("user_token",result.getData());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            setCartCookie(response, "cart_token", result.getData(), -1);
            setCartCookie(response, "old_cart_token", token, 15 * 24 * 60 * 60);
            response.addCookie(cookie);
            Map<String,String> map = new HashMap<>(2);
            map.put("noLoginKey",token);
            map.put("loginKey",result.getData());
            rabbitTemplate.convertAndSend("login-exchange","login",map);
            String url = request.getHeader("Referer");
            return "redirect:"+url;
        }
        return "index";
    }

    private void setCartCookie(HttpServletResponse response, String cart_token, String data, int i) {
        Cookie cookie1 = new Cookie(cart_token, data);
        cookie1.setPath("/");
        cookie1.setHttpOnly(true);
        if(i>=0){
            cookie1.setMaxAge(i);
        }
        response.addCookie(cookie1);
    }

    @RequestMapping("logout")
    @ResponseBody
    @CrossOrigin(origins = "*",allowCredentials = "true")
    public ResultBean<Boolean> logout(@CookieValue(name = "user_token",required = false) String token,
                                      HttpServletResponse response,
                                      @CookieValue(name = "user_token",required = false) String oldCartToken){
        setCartCookie(response, "user_token", token, 0);
        setCartCookie(response, "cart_token", oldCartToken, 15 * 24 * 60 * 60);
        setCartCookie(response, "old_cart_token", token, 0);
        return new ResultBean<>(200,true);
    }

    @RequestMapping("checkIsLogin")
    @ResponseBody
    @CrossOrigin(origins = "*",allowCredentials = "true")
    public ResultBean<User> checkIsLogin(@CookieValue(name = "user_token",required = false) String uuid, HttpServletResponse response){
        return userService.checkIsLogin(uuid);
    }


}
