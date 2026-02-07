package com.example.backenddemo.entity;

import lombok.Data;

/**
 * 登录响应实体类
 * 用于返回登录结果
 */
@Data
public class LoginResponse {
    /**
     * 是否登录成功
     */
    private Boolean success;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * JWT令牌
     */
    private String token;
    
    /**
     * 用户信息
     */
    private Object userInfo;
    
    public LoginResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public LoginResponse(Boolean success, String message, String token, Object userInfo) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.userInfo = userInfo;
    }
}