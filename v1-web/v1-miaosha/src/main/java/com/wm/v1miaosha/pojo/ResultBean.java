package com.wm.v1miaosha.pojo;

import com.github.houbb.lombok.ex.annotation.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author weimin
 * @ClassName ResultBean
 * @Description TODO
 * @date 2019/10/29 20:47
 */
@Data
@Serial
@AllArgsConstructor
public class ResultBean<T> {
    private int statusCode;
    private T data;
}
