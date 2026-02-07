package com.example.backenddemo.mapper;

import com.example.backenddemo.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    /**
     * 根据订单ID查询订单项列表
     */
    List<OrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单ID和产品ID查询订单项
     */
    OrderItem selectByOrderIdAndProductId(@Param("orderId") Long orderId, @Param("productId") Long productId);

    /**
     * 批量插入订单项
     */
    int batchInsert(List<OrderItem> orderItems);

    /**
     * 根据订单ID删除订单项
     */
    int deleteByOrderId(Long orderId);

    /**
     * 根据订单ID统计订单项数量
     */
    int countByOrderId(Long orderId);
}