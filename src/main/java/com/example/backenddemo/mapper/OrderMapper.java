package com.example.backenddemo.mapper;

import com.example.backenddemo.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 根据ID查询订单
     */
    Order selectById(Long id);

    /**
     * 根据订单号查询订单
     */
    Order selectByOrderNo(String orderNo);

    /**
     * 根据用户ID查询订单列表
     */
    List<Order> selectByUserId(@Param("userId") Long userId);

    /**
     * 查询所有订单
     */
    List<Order> selectAll();

    /**
     * 分页查询订单
     */
    List<Order> selectByPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 按条件搜索订单
     */
    List<Order> selectByCondition(@Param("orderNo") String orderNo, @Param("userId") Long userId, @Param("orderStatus") Integer orderStatus);

    /**
     * 新增订单
     */
    int insert(Order order);

    /**
     * 更新订单
     */
    int update(Order order);

    /**
     * 更新订单状态
     */
    int updateOrderStatus(@Param("orderNo") String orderNo, @Param("orderStatus") Integer orderStatus);

    /**
     * 获取订单总数
     */
    int count();
}