package com.wm.mapper;

import com.wm.base.IBaseDao;
import com.wm.entity.ErrorEmail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ErrorEmailMapper extends IBaseDao<ErrorEmail> {

    Long addExceptionMessage(@Param("to") String to, @Param("content") String content, @Param("typeId") int typeId);

    List<ErrorEmail> getRetryList(@Param("retryNum") int retryNum);

    int deleteByToEmailType(@Param("to") String to, @Param("type") int type);
}