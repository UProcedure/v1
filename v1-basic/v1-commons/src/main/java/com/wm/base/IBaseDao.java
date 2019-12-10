package com.wm.base;

import java.util.List;

/**
 * @author weimin
 */
public interface IBaseDao<T> {

    int deleteByPrimaryKey(Long id);

    int insert(T t);

    int insertSelective(T t);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T t);

    int updateByPrimaryKey(T t);

    public List<T> getList();
}
