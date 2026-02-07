package com.example.backenddemo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderItem {
    private Long id;
    private Long orderId; // 订单ID
    private Long productId; // 产品ID
    private String productName; // 产品名称
    private BigDecimal price; // 单价
    private Integer quantity; // 数量
    private BigDecimal subtotal; // 小计
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}