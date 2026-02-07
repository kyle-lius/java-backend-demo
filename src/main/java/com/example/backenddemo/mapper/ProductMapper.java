package com.example.backenddemo.mapper;

import com.example.backenddemo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    /**
     * 根据ID查询产品
     */
    Product selectById(Long id);

    /**
     * 查询所有产品
     */
    List<Product> selectAll();

    /**
     * 分页查询产品
     */
    List<Product> selectByPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 按条件搜索产品
     */
    List<Product> selectByCondition(@Param("name") String name, @Param("category") String category, @Param("status") Integer status);

    /**
     * 新增产品
     */
    int insert(Product product);

    /**
     * 更新产品
     */
    int update(Product product);

    /**
     * 删除产品（逻辑删除）
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 获取产品总数
     */
    int count();
}