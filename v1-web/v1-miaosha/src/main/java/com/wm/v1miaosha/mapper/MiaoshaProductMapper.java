package com.wm.v1miaosha.mapper;

import com.wm.v1miaosha.entity.MiaoshaProduct;

import java.util.List;

public interface MiaoshaProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MiaoshaProduct record);

    int insertSelective(MiaoshaProduct record);

    MiaoshaProduct selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoshaProduct record);

    int updateByPrimaryKey(MiaoshaProduct record);

    List<MiaoshaProduct> getCanStartKillProduct();

    List<MiaoshaProduct> getStopKillProduct();

    int updateCount(Long id);
}