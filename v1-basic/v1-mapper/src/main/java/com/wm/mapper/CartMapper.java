package com.wm.mapper;

import com.wm.base.IBaseDao;
import com.wm.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper extends IBaseDao<Cart> {
    List<Cart> getListByUserId(Long id);

    int delByProductIdUserId(@Param("userId") Long userId, @Param("productId") Long productId);
}