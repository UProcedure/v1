package com.wm.entity;

import java.util.Date;

import com.github.houbb.lombok.ex.annotation.Serial;
import lombok.Data;

@Data
@Serial
public class User {
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private Byte flag;

    private Date createTime;

    private Date updateTime;
}