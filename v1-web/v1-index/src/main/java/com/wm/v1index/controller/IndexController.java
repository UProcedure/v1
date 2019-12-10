package com.wm.v1index.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.wm.entity.ProductType;
import com.wm.service.IProductTypeService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author weimin
 * @ClassName IndexController
 * @Description TODO
 * @date 2019/11/1 17:13
 */
@Controller
@RequestMapping("index")
public class IndexController {
    @Reference
    private IProductTypeService productTypeService;

    @RequestMapping("show")
    public String productTypeList(Model model){
        List<ProductType> list =productTypeService.getList();
        model.addAttribute("list",list);
        return "index";
    }
}
