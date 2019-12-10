package com.wm.base;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author weimin
 */
public interface IBaseService<T> {
    int deleteByPrimaryKey(Long id);

    int insert(T t);

    int insertSelective(T t);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T t);

    int updateByPrimaryKey(T t);

    List<T> getList();

    PageInfo<T> getPageList(Integer pageIndex,Integer pageSize,Integer navigatePages);
}
