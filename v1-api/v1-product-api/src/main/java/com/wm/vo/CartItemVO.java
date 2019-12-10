package com.wm.vo;

import com.wm.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangguizhao
 * 跟视图层对应的对象 view Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemVO implements Serializable{

    private Product product;

    private Integer count;

    private Date updateTime;
}
