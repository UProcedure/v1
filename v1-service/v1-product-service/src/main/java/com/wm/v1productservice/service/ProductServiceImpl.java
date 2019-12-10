package com.wm.v1productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.wm.base.BaseServiceImpl;
import com.wm.base.IBaseDao;
import com.wm.entity.Product;
import com.wm.entity.ProductDesc;
import com.wm.mapper.ProductDescMapper;
import com.wm.mapper.ProductMapper;
import com.wm.service.IProductService;
import com.wm.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author weimin
 * @ClassName ProductServiceImpl
 * @Description TODO
 * @date 2019/10/28 21:24
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl<Product> implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductDescMapper productDescMapper;

    @Override
    public IBaseDao<Product> getBaseDao() {
        return productMapper;
    }

    @Override
    @Transactional
    public Long addProduct(ProductVO vo) {
        productMapper.insertSelective(vo.getProduct());
        ProductDesc productDesc = new ProductDesc();
        productDesc.setProductId(vo.getProduct().getId());
        productDesc.setProductDesc(vo.getProductDesc());
        int i = productDescMapper.insertSelective(productDesc);
        return vo.getProduct().getId();
    }

    @Override
    public int delByIds(List<Long> ids) {
        return productMapper.delByIds(ids);
    }

    @Override
    public ProductVO toUpdate(Long id) {
        Product product = productMapper.selectByPrimaryKey(id);
        String productDesc = productDescMapper.selectByProductId(id);
        return new ProductVO(product,productDesc);
    }

    @Override
    @Transactional
    public void updateProduct(ProductVO vo) {
        productMapper.updateByPrimaryKeySelective(vo.getProduct());
        productDescMapper.updateByProductId(vo.getProduct().getId(),vo.getProductDesc());
    }

    @Override
    public List<Product> getListByIds(List<Long> ids) {
        return productDescMapper.getListByIds(ids);
    }
}
