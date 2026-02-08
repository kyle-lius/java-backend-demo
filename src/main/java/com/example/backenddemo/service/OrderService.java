package com.example.backenddemo.service;

import com.example.backenddemo.entity.*;
import com.example.backenddemo.mapper.OrderItemMapper;
import com.example.backenddemo.mapper.OrderMapper;
import com.example.backenddemo.mapper.ProductMapper;
import com.example.backenddemo.util.OrderNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderNoGenerator orderNoGenerator;

    /**
     * 根据ID查询订单
     */
    public Order getOrderById(Long id) {
        return orderMapper.selectById(id);
    }

    /**
     * 根据订单号查询订单
     */
    public Order orderByOrderNo(String orderNo) {
        return orderMapper.selectByOrderNo(orderNo);
    }

    /**
     * 根据用户ID查询订单列表
     */
    public List<Order> getOrdersByUserId(Long userId) {
        return orderMapper.selectByUserId(userId);
    }

    /**
     * 查询所有订单（不包含ID字段）
     */
    public List<OrderWithoutId> getAllOrdersWithoutId() {
        List<Order> orders = orderMapper.selectAll();
        return orders.stream()
                .map(OrderWithoutId::fromOrder)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 分页查询订单（不包含ID字段）
     */
    public List<OrderWithoutId> getOrdersByPageWithoutId(int page, int size) {
        int offset = (page - 1) * size;
        List<Order> orders = orderMapper.selectByPage(offset, size);
        return orders.stream()
                .map(OrderWithoutId::fromOrder)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 按条件搜索订单（不包含ID字段）
     */
    public List<OrderWithoutId> searchOrdersWithoutId(String orderNo, Long userId, Integer orderStatus) {
        List<Order> orders = orderMapper.selectByCondition(orderNo, userId, orderStatus);
        return orders.stream()
                .map(OrderWithoutId::fromOrder)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 创建订单返回信息类
     */
    public static class OrderCreateResult {
        private Long orderId;
        private String orderNo;
        private BigDecimal totalPrice;
        
        public OrderCreateResult(Long orderId, String orderNo, BigDecimal totalPrice) {
            this.orderId = orderId;
            this.orderNo = orderNo;
            this.totalPrice = totalPrice;
        }
        
        // getter方法
        public Long getOrderId() { return orderId; }
        public String getOrderNo() { return orderNo; }
        public BigDecimal getTotalPrice() { return totalPrice; }
    }

    /**
     * 创建订单
     */
    @Transactional
    public OrderCreateResult createOrder(Long userId, List<OrderItem> orderItems, String shippingAddress, String receiverName, String receiverPhone) {
        // 验证库存并计算总价
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            Product product = productMapper.selectById(item.getProductId());
            if (product == null) {
                throw new RuntimeException("产品不存在: " + item.getProductId());
            }
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("产品库存不足: " + product.getName());
            }
            
            // 设置订单项信息
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(item.getQuantity()));
            item.setSubtotal(subtotal);
            totalPrice = totalPrice.add(subtotal);
        }

        // 生成订单号
        String orderNo = orderNoGenerator.generateOrderNo("NORMAL");

        // 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(1); // 待付款
        order.setShippingAddress(shippingAddress);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        // 插入订单
        int orderResult = orderMapper.insert(order);
        if (orderResult <= 0) {
            throw new RuntimeException("创建订单失败");
        }

        // 批量插入订单项
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            item.setCreateTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
        }
        
        int itemsResult = orderItemMapper.batchInsert(orderItems);
        if (itemsResult <= 0) {
            throw new RuntimeException("创建订单项失败");
        }

        // 更新产品库存
        for (OrderItem item : orderItems) {
            Product product = productMapper.selectById(item.getProductId());
            product.setStock(product.getStock() - item.getQuantity());
            product.setUpdateTime(LocalDateTime.now());
            productMapper.update(product);
        }

        // 返回订单创建结果，包含订单ID、订单号和总金额
        return new OrderCreateResult(order.getId(), orderNo, totalPrice);
    }

    /**
     * 取消订单
     */
    @Transactional
    public boolean cancelOrder(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || order.getOrderStatus() > 2) { // 已发货或完成的订单不能取消
            return false;
        }
        
        // 更新订单状态为已取消
        int result = orderMapper.updateOrderStatus(orderNo, 5);
        
        if (result > 0) {
            // 如果订单已支付但未发货，则需要恢复库存
            if (order.getOrderStatus() >= 2) {
                List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());
                for (OrderItem item : orderItems) {
                    Product product = productMapper.selectById(item.getProductId());
                    product.setStock(product.getStock() + item.getQuantity());
                    product.setUpdateTime(LocalDateTime.now());
                    productMapper.update(product);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 支付订单
     */
    public boolean payOrder(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || order.getOrderStatus() != 1) { // 只有待付款的订单可以支付
            return false;
        }
        
        int result = orderMapper.updateOrderStatus(orderNo, 2); // 已付款
        return result > 0;
    }

    /**
     * 发货订单
     */
    public boolean shipOrder(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || order.getOrderStatus() != 2) { // 只有已付款的订单可以发货
            return false;
        }
        
        int result = orderMapper.updateOrderStatus(orderNo, 3); // 已发货
        return result > 0;
    }

    /**
     * 完成订单
     */
    public boolean completeOrder(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || order.getOrderStatus() != 3) { // 只有已发货的订单可以完成
            return false;
        }
        
        int result = orderMapper.updateOrderStatus(orderNo, 4); // 已完成
        return result > 0;
    }

    /**
     * 获取订单项列表
     */
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemMapper.selectByOrderId(orderId);
    }

    /**
     * 获取订单总数
     */
    public int getOrderCount() {
        return orderMapper.count();
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        // 使用时间戳+随机数生成唯一订单号
        return "ORD" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }
}