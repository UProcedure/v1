package com.wm.v1errorservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.wm.api.IEmailErrorService;
import com.wm.base.BaseServiceImpl;
import com.wm.base.IBaseDao;
import com.wm.entity.ErrorEmail;
import com.wm.mapper.ErrorEmailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author weimin
 * @ClassName EmailErrorServiceImpl
 * @Description TODO
 * @date 2019/11/12 18:44
 */
@Service
public class EmailErrorServiceImpl extends BaseServiceImpl<ErrorEmail> implements IEmailErrorService {

    @Autowired
    private ErrorEmailMapper errorEmailMapper;


    @Override
    public IBaseDao<ErrorEmail> getBaseDao() {
        return errorEmailMapper;
    }

    @Override
    @Transactional
    public Long addExceptionMessage(String to, String content,int typeId) {
        return errorEmailMapper.addExceptionMessage(to,content,typeId);
    }

    @Override
    public List<ErrorEmail> getRetryList(int retryNum) {
        return errorEmailMapper.getRetryList(retryNum);
    }

    @Override
    public int deleteByToEmailType(String to, int type) {
        return errorEmailMapper.deleteByToEmailType(to,type);
    }
}
