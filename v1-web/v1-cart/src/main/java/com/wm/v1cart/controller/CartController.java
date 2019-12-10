package com.wm.v1cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wm.base.entity.ResultBean;
import com.wm.service.ICartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author weimin
 * @ClassName CartController
 * @Description TODO
 * @date 2019/11/15 15:11
 */
@Controller
@RequestMapping("cart")
public class CartController {

    @Reference
    private ICartService cartService;

    @RequestMapping("add/{productId}/{count}")
    @ResponseBody
    public ResultBean add(@PathVariable Long productId,
                          @PathVariable Integer count,
                          HttpServletResponse response,
                          @CookieValue(name = "cart_token", required = false) String token,
                          @CookieValue(name = "old_cart_token", required = false) String oldToken) {
        token = getToken(response, oldToken, token);
        ResultBean add = cartService.add(token, productId, count);
        return add;
    }


    @RequestMapping("del/{productId}")
    @ResponseBody
    public ResultBean del(@PathVariable Long productId,
                          HttpServletResponse response,
                          @CookieValue(name = "cart_token", required = false) String token) {
        return cartService.del(token, productId);
    }

    @RequestMapping("query")
    @ResponseBody
    public ResultBean query(HttpServletResponse response,
                            @CookieValue(name = "old_cart_token", required = false) String oldToken,
                            @CookieValue(name = "cart_token", required = false) String token) {
        token = getToken(response, oldToken, token);
        return cartService.query(token);
    }



    @RequestMapping("update/{productId}/{count}")
    @ResponseBody
    public ResultBean update(@PathVariable Long productId,
                             @PathVariable Integer count,
                             HttpServletResponse response,
                             @CookieValue(name = "cart_token", required = false) String token) {
        return cartService.update(token, productId, count);
    }

    private String getToken(HttpServletResponse response, String oldToken,String token) {
        if (token == null || "".equals(token)) {
            if (oldToken == null || "".equals(oldToken)) {
                token = UUID.randomUUID().toString();

            } else {
                token = oldToken;
            }
            Cookie cookie = new Cookie("cart_token", token);
            cookie.setMaxAge(15 * 24 * 60 * 60);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }
        return token;
    }

}
