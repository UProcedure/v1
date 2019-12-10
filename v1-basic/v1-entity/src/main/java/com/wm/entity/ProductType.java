package com.wm.entity;

import com.github.houbb.lombok.ex.annotation.Serial;
import lombok.Data;

/**
 * @author weimin
 */
@Data
@Serial
public class ProductType {
    private Integer id;

    private Integer pid;

    private String name;

    private Integer flag;
}