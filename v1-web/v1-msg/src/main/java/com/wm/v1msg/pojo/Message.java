package com.wm.v1msg.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weimin
 * @ClassName Message
 * @Description TODO
 * @date 2019/11/27 15:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message<T> {

    private String messageType;
    private T data;
}
