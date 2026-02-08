package com.example.backenddemo.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单信息（不包含ID字段）用于对外展示
 */
@Data
public class OrderWithoutId {
    private String orderNo; // 订单号
    private Long userId; // 用户ID
    private BigDecimal totalPrice; // 订单总金额
    private Integer orderStatus; // 订单状态：1-待付款，2-已付款，3-已发货，4-已完成，5-已取消
    private String shippingAddress; // 收货地址
    private String receiverName; // 收货人姓名
    private String receiverPhone; // 收货人电话
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    /**
     * 从Order实体转换为OrderWithoutId
     * @param order 原始订单实体
     * @return 不包含ID的订单对象
     */
    public static OrderWithoutId fromOrder(Order order) {
        if (order == null) {
            return null;
        }
        OrderWithoutId orderWithoutId = new OrderWithoutId();
        orderWithoutId.setOrderNo(order.getOrderNo());
        orderWithoutId.setUserId(order.getUserId());
        orderWithoutId.setTotalPrice(order.getTotalPrice());
        orderWithoutId.setOrderStatus(order.getOrderStatus());
        orderWithoutId.setShippingAddress(order.getShippingAddress());
        orderWithoutId.setReceiverName(order.getReceiverName());
        orderWithoutId.setReceiverPhone(order.getReceiverPhone());
        orderWithoutId.setCreateTime(order.getCreateTime());
        orderWithoutId.setUpdateTime(order.getUpdateTime());
        return orderWithoutId;
    }
}