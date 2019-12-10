package com.wm.v1center.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weimin
 * @ClassName WangEditorResultBean
 * @Description TODO
 * @date 2019/11/1 14:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WangEditorResultBean {
    private Integer errno;
    private String[] data;
}
