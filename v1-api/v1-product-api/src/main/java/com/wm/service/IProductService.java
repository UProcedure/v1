package com.wm.service;

import com.wm.base.IBaseService;
import com.wm.entity.Product;
import com.wm.vo.ProductVO;

import java.util.List;

/**
 * @author qq166
 */
public interface IProductService extends IBaseService<Product> {

    public Long addProduct(ProductVO vo);

    int delByIds(List<Long> ids);

    ProductVO toUpdate(Long id);

    void updateProduct(ProductVO vo);

    List<Product> getListByIds(List<Long> ids);
}
