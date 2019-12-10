package com.wm.v1productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.wm.base.BaseServiceImpl;
import com.wm.base.IBaseDao;
import com.wm.entity.ProductType;
import com.wm.mapper.ProductTypeMapper;
import com.wm.service.IProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author weimin
 * @ClassName ProductTypeServiceImpl
 * @Description TODO
 * @date 2019/11/1 17:20
 */
@Service
public class ProductTypeServiceImpl extends BaseServiceImpl<ProductType> implements IProductTypeService {

    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Resource(name = "redisTemplate1")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public IBaseDao<ProductType> getBaseDao() {
        return productTypeMapper;
    }

    @Override
    public List<ProductType> getList() {
        List<ProductType> list = (List<ProductType>) redisTemplate.opsForValue().get("productType:list");
        if(list == null || list.size()==0){
            list = super.getList();
            redisTemplate.opsForValue().set("productType:list",list);
        }
        return list;
    }
}
