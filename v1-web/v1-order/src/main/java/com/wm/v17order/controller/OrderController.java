package com.wm.v17order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author weimin
 * @ClassName OrderController
 * @Description TODO
 * @date 2019/11/18 19:56
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @RequestMapping("confirm")
    public String confirm(){
        return "confirm";
    }
}
