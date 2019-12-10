package com.wm.v17order.entity;

import lombok.Data;

/**
 * @author weimin
 * @ClassName BizContent
 * @Description TODO
 * @date 2019/11/18 17:45
 */
@Data
public class BizContent {
    private String out_trade_no;
    private String product_code;
    private String total_amount;
    private String subject;
    private String body;
}
