package com.wm.entity;

import com.github.houbb.lombok.ex.annotation.Serial;
import lombok.Data;

@Data
@Serial
public class ProductDesc {
    private Long id;

    private Long productId;

    private String productDesc;
}