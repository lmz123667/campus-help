package com.example.campushelp.controller;

import com.example.campushelp.entity.User;
import com.example.campushelp.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User loginUser) {
        Map<String, Object> result = new HashMap<>();
        User user = userMapper.findByUsername(loginUser.getUsername());

        if (user == null) {
            result.put("success", false);
            result.put("message", "用户不存在！");
            return result;
        }

        if (!user.getPassword().equals(loginUser.getPassword())) {
            result.put("success", false);
            result.put("message", "密码错误！");
            return result;
        }

        result.put("success", true);
        result.put("message", "登录成功！");
        result.put("data", user);
        return result;
    }

    /**
     * 【新增】注册接口
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();

        // 1. 检查用户名是否已存在
        User existUser = userMapper.findByUsername(user.getUsername());
        if (existUser != null) {
            result.put("success", false);
            result.put("message", "该账号已被注册，请换一个！");
            return result;
        }

        // 2. 简单的校验
        if (user.getUsername() == null || user.getPassword() == null || user.getRole() == null) {
            result.put("success", false);
            result.put("message", "账号、密码、角色不能为空！");
            return result;
        }

        // 3. 插入数据库
        try {
            userMapper.insert(user);
            result.put("success", true);
            result.put("message", "注册成功！请登录");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "注册失败: " + e.getMessage());
        }
        return result;
    }
}