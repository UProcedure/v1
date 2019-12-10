package com.wm.entity;

import java.util.Date;

import com.github.houbb.lombok.ex.annotation.Serial;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weimin
 */
@Data
@Serial
public class Product {
    private Long id;
    @NotBlank(message = "商品名字不能为空")
    private String name;
    @NotNull(message = "商品价格不能为空")
    private Long price;
    @NotNull(message = "商品折扣价不能为空")
    private Long salePrice;

    private String images;

    private String salePoint;

    private Integer flag;

    private Date createTime;

    private Date updateTime;
    @NotNull(message = "商品类型ID不能为空")
    private Integer tapeId;
    @NotBlank(message = "商品类型名不能为空")
    private String tapeName;
}