package com.wm.v1miaosha.service;

import com.wm.v1miaosha.pojo.ResultBean;

public interface IRemindService {
    ResultBean add(Long userId, Long miaoshaId);
}
