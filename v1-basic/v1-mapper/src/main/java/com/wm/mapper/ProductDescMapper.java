package com.wm.mapper;

import com.wm.base.IBaseDao;
import com.wm.entity.Product;
import com.wm.entity.ProductDesc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author weimin
 */
public interface ProductDescMapper extends IBaseDao<ProductDesc> {
    String selectByProductId(Long id);

    int updateByProductId(@Param("id") Long id, @Param("productDesc") String productDesc);

    List<Product> getListByIds(List<Long> ids);
}