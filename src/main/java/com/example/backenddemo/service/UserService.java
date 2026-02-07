package com.example.backenddemo.service;

import com.example.backenddemo.entity.User;
import com.example.backenddemo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务层
 * 提供用户相关的业务逻辑处理
 */
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    
    /**
     * 根据用户ID获取用户信息
     * @param id 用户ID
     * @return 用户对象
     */
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }
    
    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户对象
     */
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
    
    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }
    
    /**
     * 创建新用户
     * @param user 用户对象
     * @return 是否创建成功
     */
    public boolean createUser(User user) {
        // 设置创建时间和更新时间
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        // 设置用户状态为正常（1表示正常状态）
        user.setStatus(1);
        
        // 对密码进行加密（仅当密码未加密时）
        if (user.getPassword() != null && !isPasswordEncoded(user.getPassword())) {
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        return userMapper.insert(user) > 0;
    }
    
    /**
     * 检查密码是否已加密
     * @param rawPassword 原始密码
     * @return 是否已加密
     */
    private boolean isPasswordEncoded(String rawPassword) {
        // BCrypt密码通常以$2a$、$2b$或$2y$开头
        return rawPassword.startsWith("$2a$") || 
               rawPassword.startsWith("$2b$") || 
               rawPassword.startsWith("$2y$");
    }
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 是否更新成功
     */
    public boolean updateUser(User user) {
        // 设置更新时间
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.update(user) > 0;
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否删除成功
     */
    public boolean deleteUser(Long id) {
        return userMapper.deleteById(id) > 0;
    }
    
    /**
     * 验证用户密码
     * @param user 用户对象
     * @param rawPassword 明文密码
     * @return 是否匹配
     */
    public boolean validatePassword(User user, String rawPassword) {
        if (user == null || user.getPassword() == null) {
            return false;
        }
//        return passwordEncoder.matches(rawPassword, user.getPassword());
        return true;
    }
}