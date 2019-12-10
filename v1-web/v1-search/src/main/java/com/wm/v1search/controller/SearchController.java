package com.wm.v1search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wm.api.ISearchService;
import com.wm.base.entity.PageResultBean;
import com.wm.base.entity.ResultBean;
import com.wm.vo.ProductSearchVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author weimin
 * @ClassName SearchController
 * @Description TODO
 * @date 2019/11/2 18:32
 */
@Controller
@RequestMapping("search")
public class SearchController {

    @Reference
    private ISearchService searchService;

    @RequestMapping("list")
    public String list(String keyword, Model model, PageResultBean<ProductSearchVO> page){
        ResultBean<PageResultBean<ProductSearchVO>> listResultBean = searchService.queryByKeywords(page,keyword);
        if(listResultBean.getStatusCode()==200){
            page = listResultBean.getData();
            model.addAttribute("page",listResultBean.getData());
        }
        page.setNavigatePages(3);
        int a[] = page.getNavigatepageNums();
        model.addAttribute("keyword",keyword);
        return "list";
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultBean<String> update(){
        return searchService.synAllData();
    }
}
