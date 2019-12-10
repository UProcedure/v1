package com.wm.vo;

import com.github.houbb.lombok.ex.annotation.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weimin
 * @ClassName ProductSearchVO
 * @Description TODO
 * @date 2019/11/2 16:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Serial
public class ProductSearchVO {
    private Long id;

    private String name;

    private Long price;

    private Long salePrice;

    private String images;

    private String salePoint;
}
