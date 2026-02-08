package com.example.backenddemo.controller;

import com.example.backenddemo.entity.Order;
import com.example.backenddemo.entity.OrderItem;
import com.example.backenddemo.entity.OrderWithoutId;
import com.example.backenddemo.service.OrderService;
//import com.example.backenddemo.util.JwtValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 根据ID查询订单
     */
    @GetMapping("/{id}")
    public Map<String, Object> getOrderById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order order = orderService.getOrderById(id);
            if (order != null) {
                result.put("success", true);
                result.put("data", order);
                
                // 获取订单项
                List<OrderItem> orderItems = orderService.getOrderItems(order.getId());
                result.put("items", orderItems);
            } else {
                result.put("success", false);
                result.put("message", "订单不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 根据订单号查询订单
     */
    @GetMapping("/no/{orderNo}")
    public Map<String, Object> orderByOrderNo(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order order = orderService.orderByOrderNo(orderNo);
            if (order != null) {
                result.put("success", true);
                result.put("data", order);
                
                // 获取订单项
                List<OrderItem> orderItems = orderService.getOrderItems(order.getId());
                result.put("items", orderItems);
            } else {
                result.put("success", false);
                result.put("message", "订单不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 根据用户ID查询订单列表
     */
    @GetMapping("/user/{userId}")
    public Map<String, Object> getOrdersByUserId(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);
            result.put("success", true);
            result.put("data", orders);
            result.put("count", orders.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 查询所有订单（不包含ID字段）
     */
    @GetMapping("/all")
    public Map<String, Object> getAllOrders() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OrderWithoutId> orders = orderService.getAllOrdersWithoutId();
            result.put("success", true);
            result.put("data", orders);
            result.put("count", orders.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询订单（不包含ID字段）
     */
    @GetMapping("/page")
    public Map<String, Object> getOrdersByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OrderWithoutId> orders = orderService.getOrdersByPageWithoutId(page, size);
            int totalCount = orderService.getOrderCount();
            result.put("success", true);
            result.put("data", orders);
            result.put("totalCount", totalCount);
            result.put("currentPage", page);
            result.put("pageSize", size);
            result.put("totalPages", (int) Math.ceil((double) totalCount / size));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 按条件搜索订单（不包含ID字段）
     */
    @GetMapping("/search")
    public Map<String, Object> searchOrders(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer orderStatus) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OrderWithoutId> orders = orderService.searchOrdersWithoutId(orderNo, userId, orderStatus);
            result.put("success", true);
            result.put("data", orders);
            result.put("count", orders.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 创建订单
     */
    /**
     *
     * @param orderData userId, shippingAddress, receiverName, receiverPhone, items
     * @return
     */
    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> orderData) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = Long.valueOf(orderData.get("userId").toString());
            String shippingAddress = orderData.get("shippingAddress").toString();
            String receiverName = orderData.get("receiverName").toString();
            String receiverPhone = orderData.get("receiverPhone").toString();
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> itemsList = (List<Map<String, Object>>) orderData.get("items");
            
            List<OrderItem> orderItems = itemsList.stream().map(item -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(Long.valueOf(item.get("productId").toString()));
                orderItem.setQuantity(Integer.valueOf(item.get("quantity").toString()));
                return orderItem;
            }).collect(java.util.stream.Collectors.toList());
            
            // 调用服务层创建订单，获取包含订单ID和总金额的结果
            OrderService.OrderCreateResult createResult = orderService.createOrder(userId, orderItems, shippingAddress, receiverName, receiverPhone);
            
            result.put("success", true);
            result.put("message", "订单创建成功");
            // 返回订单ID和订单总金额
            Map<String, Object> orderInfo = new HashMap<>();
            orderInfo.put("orderId", createResult.getOrderId());
            orderInfo.put("orderNo", createResult.getOrderNo());
            orderInfo.put("totalPrice", createResult.getTotalPrice());
            result.put("data", orderInfo);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 取消订单
     */
    @PutMapping("/{orderNo}/cancel")
    public Map<String, Object> cancelOrder(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = orderService.cancelOrder(orderNo);
            if (success) {
                result.put("success", true);
                result.put("message", "订单取消成功");
            } else {
                result.put("success", false);
                result.put("message", "订单取消失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 支付订单
     */
    @PutMapping("/{orderNo}/pay")
    public Map<String, Object> payOrder(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = orderService.payOrder(orderNo);
            if (success) {
                result.put("success", true);
                result.put("message", "订单支付成功");
            } else {
                result.put("success", false);
                result.put("message", "订单支付失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 发货订单
     */
    @PutMapping("/{orderNo}/ship")
    public Map<String, Object> shipOrder(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = orderService.shipOrder(orderNo);
            if (success) {
                result.put("success", true);
                result.put("message", "订单发货成功");
            } else {
                result.put("success", false);
                result.put("message", "订单发货失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 完成订单
     */
    @PutMapping("/{orderNo}/complete")
    public Map<String, Object> completeOrder(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = orderService.completeOrder(orderNo);
            if (success) {
                result.put("success", true);
                result.put("message", "订单完成成功");
            } else {
                result.put("success", false);
                result.put("message", "订单完成失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 获取当前用户的订单（需要JWT验证）
     */
    @GetMapping("/my-orders")
    public Map<String, Object> getMyOrders(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        
        // 使用公共JWT验证方法验证令牌
        Map<String, Object> validation = null;//jwtValidationHelper.validateTokenAndGetUserInfo(token);
        
        if (!(Boolean) validation.get("valid")) {
            result.put("success", false);
            result.put("message", validation.get("message"));
            result.put("code", validation.get("code"));
            return result;
        }
        
        String username = (String) validation.get("username");
        try {
            // 在实际应用中，这里会根据用户名获取用户ID，然后查询该用户的订单
            // 这里简化处理，仅作为示例
            List<Order> orders = orderService.getOrdersByUserId(1L); // 示例用户ID
            result.put("success", true);
            result.put("data", orders);
            result.put("count", orders.size());
            result.put("message", "获取订单成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取订单失败: " + e.getMessage());
        }
        return result;
    }
}