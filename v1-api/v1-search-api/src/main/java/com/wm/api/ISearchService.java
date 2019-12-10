package com.wm.api;

import com.wm.base.entity.PageResultBean;
import com.wm.base.entity.ResultBean;
import com.wm.vo.ProductSearchVO;

import java.util.List;

/**
 * @author weimin
 */
public interface ISearchService {
    public ResultBean<String> synAllData();

    public ResultBean<String> synById(Long id);

    public ResultBean<String> delById(Long id);

    public ResultBean<PageResultBean<ProductSearchVO>> queryByKeywords(PageResultBean<ProductSearchVO> page,String keywords);
}
