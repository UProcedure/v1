package com.wm.entity;

import java.util.Date;

import com.github.houbb.lombok.ex.annotation.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Serial
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private Long id;

    private Long userId;

    private Long productId;

    private Date updateTime;

    private Integer count;

    public Cart(Long userId, Long productId, Date updateTime, Integer count) {
        this.userId = userId;
        this.productId = productId;
        this.updateTime = updateTime;
        this.count = count;
    }

    public Cart(Long productId, Date updateTime, Integer count) {
        this.productId = productId;
        this.updateTime = updateTime;
        this.count = count;
    }
}