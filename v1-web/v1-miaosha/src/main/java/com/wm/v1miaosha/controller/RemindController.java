package com.wm.v1miaosha.controller;

import com.wm.v1miaosha.pojo.ResultBean;
import com.wm.v1miaosha.service.IRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weimin
 * @ClassName RemindController
 * @Description TODO
 * @date 2019/11/27 18:53
 */
@RestController
@RequestMapping("remind")
public class RemindController {

    @Autowired
    private IRemindService remindService;

    @RequestMapping("add/{userId}/{miaoshaId}")
    public ResultBean add(@PathVariable Long userId, @PathVariable Long miaoshaId){
        return remindService.add(userId,miaoshaId);
    }

}
