package com.wm.service;

import com.wm.base.IBaseService;
import com.wm.base.entity.ResultBean;
import com.wm.entity.Cart;

/**
 * @author weimin
 */
public interface ICartService extends IBaseService<Cart> {

    public ResultBean add(String token,Long productId,int count);
    public ResultBean del(String token,Long productId);
    public ResultBean query(String token);
    public ResultBean update(String token,Long productId,int count);
    public ResultBean combine(String token,String userToken);
}
