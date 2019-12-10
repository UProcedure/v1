package com.wm.mapper;

import com.wm.base.IBaseDao;
import com.wm.entity.Cart;
import com.wm.entity.Product;
import com.wm.vo.ProductSearchVO;

import java.util.List;

/**
 * @author weimin
 */
public interface ProductMapper extends IBaseDao<Product> {
    int delByIds(List<Long> ids);

    List<ProductSearchVO> getSearchList();

    int batchAddCart(List<Cart> list);
}