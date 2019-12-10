package com.wm.api;

import com.wm.base.IBaseService;
import com.wm.base.entity.ResultBean;
import com.wm.entity.User;

/**
 * @author weimin
 */
public interface IUserService extends IBaseService<User>{

    /**
     *TODO: 用户名唯一性校验
     *@author weimin
     *@date 2019/11/11
     *@param username
     *@return ResultBean
     */
    public ResultBean<Boolean> checkUserNameIsExist(String username);
    /**
     *TODO: 手机号码唯一性校验
     *@author weimin
     *@date 2019/11/11
     *@param phone
     *@return
     */
    public ResultBean<Boolean> checkPhoneIsExist(String phone);
    /**
     *TODO: 邮箱唯一校验
     *@author weimin
     *@date 2019/11/11
     *@param email
     *@return
     */
    public ResultBean<Boolean> checkEmailIsExist(String email);

    int activating(String email);

    ResultBean<String> loginAuthentication(String username, String password);

    ResultBean<Boolean> logout(String uuid);

    ResultBean<User> checkIsLogin(String uuid);
}
