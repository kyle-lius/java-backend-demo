package com.example.backenddemo.mapper;

import com.example.backenddemo.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户数据访问层接口
 * 定义了对用户表的各种数据库操作方法
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据用户ID查询用户信息
     * @param id 用户ID
     * @return 用户对象，如果不存在则返回null
     */
    User selectById(Long id);
    
    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户对象，如果不存在则返回null
     */
    User selectByUsername(String username);
    
    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> selectAll();
    
    /**
     * 插入新用户
     * @param user 用户对象
     * @return 影响的行数
     */
    int insert(User user);
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 影响的行数
     */
    int update(User user);
    
    /**
     * 根据用户ID删除用户
     * @param id 用户ID
     * @return 影响的行数
     */
    int deleteById(Long id);
}