package com.example.backenddemo.controller;

import com.example.backenddemo.entity.LoginRequest;
import com.example.backenddemo.entity.LoginResponse;
import com.example.backenddemo.entity.User;
import com.example.backenddemo.service.AuthService;
import com.example.backenddemo.service.UserService;
//import com.example.backenddemo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 提供登录、注册等认证相关的API接口
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // 允许跨域请求
public class AuthController {

    @Autowired
    private AuthService authService;

//    @Autowired
//    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param loginRequest 登录请求参数
     * @return 登录响应结果
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> result = new HashMap<>();
        System.out.println("login " + loginRequest.getUsername() + " " + loginRequest.getPassword());
        try {
            LoginResponse response = authService.login(loginRequest);
            result.put("success", response.getSuccess());
            result.put("message", response.getMessage());
            if (response.getSuccess()) {
                result.put("token", response.getToken());
                result.put("userInfo", response.getUserInfo());
                result.put("code", 200);
            } else {
                result.put("code", 401);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "登录失败: " + e.getMessage());
            result.put("code", 500);
        }
        return result;
    }

    /**
     * 用户注册
     * @param user 用户注册信息
     * @return 注册响应结果
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            LoginResponse response = authService.register(user);
            result.put("success", response.getSuccess());
            result.put("message", response.getMessage());
            if (response.getSuccess()) {
                result.put("code", 200);
            } else {
                result.put("code", 400);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "注册失败: " + e.getMessage());
            result.put("code", 500);
        }
        return result;
    }

    /**
     * 获取当前登录用户信息
     * @param token 从请求头获取的JWT令牌
     * @return 当前用户信息
     */
    @GetMapping("/profile")
    public Map<String, Object> getCurrentUser(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 去掉Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 从JWT中获取用户名
            String username = "";//jwtUtil.getUsernameFromToken(token);
            if (username != null && authService.validateToken(token, username)) {
                // 根据用户名获取用户信息
                User user = userService.getUserByUsername(username);
                if (user != null) {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("id", user.getId());
                    userInfo.put("username", user.getUsername());
                    userInfo.put("email", user.getEmail());
                    userInfo.put("phone", user.getPhone());
                    userInfo.put("status", user.getStatus());
                    userInfo.put("createTime", user.getCreateTime());
                    
                    result.put("success", true);
                    result.put("message", "获取用户信息成功");
                    result.put("userInfo", userInfo);
                    result.put("code", 200);
                } else {
                    result.put("success", false);
                    result.put("message", "用户不存在");
                    result.put("code", 404);
                }
            } else {
                result.put("success", false);
                result.put("message", "无效的令牌");
                result.put("code", 401);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取用户信息失败: " + e.getMessage());
            result.put("code", 500);
        }
        return result;
    }

    /**
     * 退出登录
     * @return 退出结果
     */
    @PostMapping("/logout")
    public Map<String, Object> logout() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "退出登录成功");
        result.put("code", 200);
        return result;
    }
}