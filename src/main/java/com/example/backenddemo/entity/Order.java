package com.example.backenddemo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private String orderNo; // 订单号
    private Long userId; // 用户ID
    private BigDecimal totalPrice; // 订单总金额
    private Integer orderStatus; // 订单状态：1-待付款，2-已付款，3-已发货，4-已完成，5-已取消
    private String shippingAddress; // 收货地址
    private String receiverName; // 收货人姓名
    private String receiverPhone; // 收货人电话
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}