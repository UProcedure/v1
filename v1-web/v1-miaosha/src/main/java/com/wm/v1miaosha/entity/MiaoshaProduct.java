package com.wm.v1miaosha.entity;

import java.util.Date;

import com.github.houbb.lombok.ex.annotation.Serial;
import lombok.Data;

@Data
@Serial
public class MiaoshaProduct {
    private Long id;

    private Long productId;

    private String productName;

    private String productImages;

    private Integer productTypeId;

    private String productTypeName;

    private Integer productPrice;

    private Long salePrice;

    private Integer count;

    private Date startTime;

    private Date endTime;

    private String status;

    private Boolean check;

    private Boolean flag;

    private Date createTime;

    private Date updateTime;

    private Long createUser;

    private Long updateUser;
}