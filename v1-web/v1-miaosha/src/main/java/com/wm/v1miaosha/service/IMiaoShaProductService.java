package com.wm.v1miaosha.service;

import com.wm.v1miaosha.entity.MiaoshaProduct;
import com.wm.v1miaosha.pojo.ResultBean;

import java.util.List;

/**
 * @author
 */
public interface IMiaoShaProductService {
    MiaoshaProduct getById(long id);

    List<MiaoshaProduct> getCanStartKillProduct();

    ResultBean kill(Long miaoshaId, Long userId, String path);

    ResultBean getMiaoshaPath(Long miaoshaId, Long userId);

    void test();
}
