package com.example.backenddemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日志实体类
 * 用于记录系统操作日志
 */
@Data
public class Log {
    /**
     * 日志ID，主键
     */
    private Long id;
    
    /**
     * 操作类型（如：查询、创建、更新、删除）
     */
    private String operationType;
    
    /**
     * 操作模块（如：用户、产品、订单）
     */
    private String module;
    
    /**
     * 操作描述
     */
    private String description;
    
    /**
     * 操作用户ID
     */
    private Long userId;
    
    /**
     * 操作用户名称
     */
    private String userName;
    
    /**
     * IP地址
     */
    private String ipAddress;
    
    /**
     * 请求方法
     */
    private String method;
    
    /**
     * 请求URL
     */
    private String url;
    
    /**
     * 请求参数
     */
    private String params;
    
    /**
     * 返回结果
     */
    private String result;
    
    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;
    
    /**
     * 异常信息
     */
    private String exception;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}