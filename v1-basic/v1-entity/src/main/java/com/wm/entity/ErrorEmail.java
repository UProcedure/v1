package com.wm.entity;

import java.util.Date;

import com.github.houbb.lombok.ex.annotation.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Serial
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEmail {
    private Long id;

    private String toEmail;

    private String params;

    private Date createTime;

    private Integer retryNum;

    private Integer typeId;
}