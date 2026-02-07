package com.example.backenddemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 用于表示系统中的用户信息
 */
@Data
public class User {
    /**
     * 用户ID，主键
     */
    private Long id;
    
    /**
     * 用户名，唯一标识
     */
    private String username;
    
    /**
     * 密码，加密存储
     */
    private String password;
    
    /**
     * 邮箱地址
     */
    private String email;
    
    /**
     * 手机号码
     */
    private String phone;
    
    /**
     * 用户状态：1-正常，0-禁用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}