package com.wm.mapper;

import com.wm.base.IBaseDao;
import com.wm.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends IBaseDao<User> {

    User getUserByUserName(String username);

    User getUserByPhone(String phone);

    User getUserByEmail(String email);

    int activating(String email);

    User loginAuthentication(@Param("username") String username);
}