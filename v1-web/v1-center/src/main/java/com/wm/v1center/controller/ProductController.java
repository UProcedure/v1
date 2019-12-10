package com.wm.v1center.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.wm.base.entity.ResultBean;
import com.wm.constant.MQConstant;
import com.wm.entity.Product;
import com.wm.service.IProductService;
import com.wm.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author weimin
 * @ClassName ProductController
 * @Description TODO
 * @date 2019/10/28 21:28
 */
@Controller
@RequestMapping("product")
@Slf4j
@Component
public class ProductController {

    @Reference
    private IProductService productService;
    @Autowired
    private RabbitTemplate template;

    @RequestMapping("get/{id}")
    @ResponseBody
    public Product get(@PathVariable Long id){
        return productService.selectByPrimaryKey(id);
    }

    @RequestMapping("list/{pageIndex}/{pageSize}")
    public String list(@PathVariable("pageIndex") Integer pageIndex,
                       @PathVariable("pageSize") Integer pageSize, ModelMap modelMap){
        PageInfo<Product> pageList = productService.getPageList(pageIndex, pageSize, 3);
        modelMap.put("page",pageList);
        return "product/list";
    }

    @PostMapping("add")
    public String add(ProductVO vo){
        Long id = productService.addProduct(vo);
        template.convertAndSend(MQConstant.EXCHANGE.V1_PRODUCT_EXCHANGE,"product-add",id);
        return "redirect:/product/list/1/1";
    }

    @PostMapping("delByIds")
    @ResponseBody
    public ResultBean delByIds(@RequestParam List<Long> ids){
        System.out.println(ids);
        int count = productService.delByIds(ids);
        if(count > 0){
            return new ResultBean(200,"删除成功");
        }
        return new ResultBean(500,"删除失败");
    }

    @PostMapping("del/{id}")
    @ResponseBody
    public ResultBean del(@PathVariable("id") Long id){
        int count = productService.deleteByPrimaryKey(id);
        if(count > 0){
            return new ResultBean(200,"删除成功");
        }
        return new ResultBean(500,"删除失败");
    }

    @RequestMapping("toUpdate/{id}")
    @ResponseBody
    public ProductVO toUpdate(@PathVariable Long id){
        return productService.toUpdate(id);
    }

    @PostMapping("update/{pageIndex}")
    public String update(ProductVO vo,@PathVariable Integer pageIndex){
        log.info(""+vo);
        productService.updateProduct(vo);
        return "redirect:/product/list/"+pageIndex+"/1";
    }

    @RequestMapping("test")
    @ResponseBody
    public String test(){
        String a = "这是一条信息";
        template.convertAndSend("v1-fanout-exchange","",a);
        return "ok";
    }

}
