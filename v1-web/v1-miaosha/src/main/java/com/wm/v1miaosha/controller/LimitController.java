package com.wm.v1miaosha.controller;

import com.wm.v1miaosha.pojo.ResultBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author weimin
 * @ClassName LimitController
 * @Description TODO
 * @date 2019/11/22 19:23
 */
@Controller
@RequestMapping("limit")
public class LimitController {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("update")
    @ResponseBody
    public ResultBean update(Integer count){
        rabbitTemplate.convertAndSend("miaosha:exchange","miaosha:limit",count);
        return new ResultBean(200,true);
    }

}
