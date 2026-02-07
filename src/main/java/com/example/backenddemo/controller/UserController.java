package com.example.backenddemo.controller;

import com.example.backenddemo.entity.User;
import com.example.backenddemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 * 提供用户相关的REST API接口
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 根据用户ID获取用户信息
     * @param id 用户ID
     * @return 包含用户信息的响应结果
     */
    @GetMapping("/{id}")
    public Map<String, Object> getUserById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            User user = userService.getUserById(id);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", user);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 包含用户信息的响应结果
     */
    @GetMapping("/username/{username}")
    public Map<String, Object> getUserByUsername(@PathVariable String username) {
        Map<String, Object> result = new HashMap<>();
        try {
            User user = userService.getUserByUsername(username);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", user);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取所有用户列表
     * @return 包含用户列表的响应结果
     */
    @GetMapping("/all")
    public Map<String, Object> getAllUsers() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> users = userService.getAllUsers();
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", users);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 创建新用户
     * @param user 用户对象
     * @return 包含操作结果的响应结果
     */
    @PostMapping
    public Map<String, Object> createUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = userService.createUser(user);
            if (success) {
                result.put("code", 200);
                result.put("message", "用户创建成功");
            } else {
                result.put("code", 500);
                result.put("message", "用户创建失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "创建失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 包含操作结果的响应结果
     */
    @PutMapping
    public Map<String, Object> updateUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = userService.updateUser(user);
            if (success) {
                result.put("code", 200);
                result.put("message", "用户更新成功");
            } else {
                result.put("code", 500);
                result.put("message", "用户更新失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "更新失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 包含操作结果的响应结果
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = userService.deleteUser(id);
            if (success) {
                result.put("code", 200);
                result.put("message", "用户删除成功");
            } else {
                result.put("code", 500);
                result.put("message", "用户删除失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "删除失败: " + e.getMessage());
        }
        return result;
    }
}