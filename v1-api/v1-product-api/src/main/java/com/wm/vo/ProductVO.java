package com.wm.vo;

import com.github.houbb.lombok.ex.annotation.Serial;
import com.wm.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weimin
 * @ClassName ProductVO
 * @Description TODO
 * @date 2019/10/29 18:55
 */
@Data
@Serial
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO {
    private Product product;
    private String productDesc;
}
