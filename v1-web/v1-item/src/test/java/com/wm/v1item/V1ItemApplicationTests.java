package com.wm.v1item;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wm.entity.Product;
import com.wm.service.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class V1ItemApplicationTests {

    @Reference
    private IProductService productService;


    @Test
    public void contextLoads() {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(6L);
        list.add(7L);
        List<Product> listByIds = productService.getListByIds(list);
        System.out.println(listByIds.size());
    }

}
