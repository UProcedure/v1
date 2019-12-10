package com.wm.v1miaosha.controller;

import com.wm.v1miaosha.exception.MiaoshaException;
import com.wm.v1miaosha.pojo.ResultBean;
import com.wm.v1miaosha.service.IMiaoShaProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author weimin
 * @ClassName MiaoShaController
 * @Description TODO
 * @date 2019/11/20 14:26
 */
@Controller
public class MiaoShaController {

    @Autowired
    private IMiaoShaProductService miaoShaProductService;

    @RequestMapping("getPath")
    @ResponseBody
    public ResultBean getPath(Long miaoshaId, Long userId){
        try {
            return miaoShaProductService.getMiaoshaPath(miaoshaId,userId);
        }catch (MiaoshaException e){
            return new ResultBean(404,e.getMessage());
        }
    }

    @RequestMapping("kill/{path}")
    @ResponseBody
    public ResultBean kill(Long miaoshaId, Long userId, @PathVariable String path){
        try {
            return miaoShaProductService.kill(miaoshaId,userId,path);
        }catch (MiaoshaException e){
            return new ResultBean(404,e.getMessage());
        }
    }


    @RequestMapping("test")
    @ResponseBody
    public String test(){
        miaoShaProductService.test();
        return "ok";
    }

}
