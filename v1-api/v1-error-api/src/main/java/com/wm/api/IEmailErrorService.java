package com.wm.api;

import com.wm.base.IBaseService;
import com.wm.entity.ErrorEmail;

import java.util.List;

public interface IEmailErrorService extends IBaseService<ErrorEmail> {
    Long addExceptionMessage(String to, String content,int typeId);

    List<ErrorEmail> getRetryList(int retryNum);

    int deleteByToEmailType(String to, int type);
}
