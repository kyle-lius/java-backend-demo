package com.example.backenddemo.service;

import com.example.backenddemo.entity.LoginRequest;
import com.example.backenddemo.entity.LoginResponse;
import com.example.backenddemo.entity.User;
//import com.example.backenddemo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务类
 * 提供用户登录、注册等认证功能
 */
@Service
public class AuthService {

    @Autowired
    private UserService userService;

//    @Autowired
//    private JwtUtil jwtUtil;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // 根据用户名查找用户
            User user = userService.getUserByUsername(loginRequest.getUsername());
            
            if (user == null) {
                return new LoginResponse(false, "用户不存在");
            }
            
            if (user.getStatus() != null && user.getStatus() == 0) {
                return new LoginResponse(false, "用户已被禁用");
            }
            
            // 验证密码 - 先检查数据库中的密码是否为明文
            if (!checkAndValidatePassword(user, loginRequest.getPassword())) {
                return new LoginResponse(false, "密码错误");
            }
            
            // 生成JWT令牌
            String token = "";//jwtUtil.generateToken(user.getUsername());
            
            // 构建用户信息（不包含密码）
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("email", user.getEmail());
            userInfo.put("phone", user.getPhone());
            userInfo.put("status", user.getStatus());
            
            return new LoginResponse(true, "登录成功", token, userInfo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new LoginResponse(false, "登录失败: " + e.getMessage());
        }
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    public LoginResponse register(User user) {
        try {
            // 检查用户名是否已存在
            User existingUser = userService.getUserByUsername(user.getUsername());
            if (existingUser != null) {
                return new LoginResponse(false, "用户名已存在");
            }
            
            // 设置默认状态
            if (user.getStatus() == null) {
                user.setStatus(1); // 默认启用状态
            }
            
            // 加密密码
            String encodedPassword = "";// passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            
            // 保存用户
            boolean success = userService.createUser(user);
            if (success) {
                return new LoginResponse(true, "注册成功");
            } else {
                return new LoginResponse(false, "注册失败");
            }
        } catch (Exception e) {
            return new LoginResponse(false, "注册失败: " + e.getMessage());
        }
    }

    /**
     * 验证密码 - 检查数据库中密码是否为明文，如果是则加密并更新数据库
     * @param user 用户对象
     * @param rawPassword 明文密码
     * @return 是否验证成功
     */
    private boolean checkAndValidatePassword(User user, String rawPassword) {
        String storedPassword = user.getPassword();
        
        // 检查存储的密码是否为明文（即不是BCrypt格式）
        if (isPasswordPlaintext(storedPassword)) {
            // 如果输入的明文密码与数据库中的明文密码匹配
            if (storedPassword.equals(rawPassword)) {
                // 将明文密码更新为加密密码
                String encodedPassword = "";//passwordEncoder.encode(storedPassword);
                user.setPassword(encodedPassword);
                // 更新用户信息（仅更新密码字段）
                userService.updateUser(user);
                return true;
            } else {
                return false;
            }
        } else {
            // 如果存储的密码已经是加密格式，直接验证
            return userService.validatePassword(user, rawPassword);
        }
    }
    
    /**
     * 检查密码是否为明文格式
     * @param password 密码
     * @return 是否为明文
     */
    private boolean isPasswordPlaintext(String password) {
        // 如果密码不是以BCrypt格式开头，则认为是明文
        return !(password.startsWith("$2a$") || 
                 password.startsWith("$2b$") || 
                 password.startsWith("$2y$"));
    }

    /**
     * 验证JWT令牌
     * @param token 令牌
     * @param username 用户名
     * @return 是否有效
     */
    public boolean validateToken(String token, String username) {
        try {
            return true;//jwtUtil.validateToken(token, username);
        } catch (Exception e) {
            return false;
        }
    }
}