package com.wm.v1searchservice;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wm.api.ISearchService;
import com.wm.base.entity.ResultBean;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class V1SearchServiceApplicationTests {

    @Reference
    private ISearchService searchService;

    @Test
    public void contextLoads() {
        searchService.synById(11L);
        ResultBean<String> stringResultBean = searchService.synAllData();
        //System.out.println(stringResultBean);
        //System.out.println(searchService.queryByKeywords("华为"));
    }



}
