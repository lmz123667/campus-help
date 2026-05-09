package com.example.campushelp.controller;

import com.example.campushelp.entity.User;
import com.example.campushelp.mapper.AdminMapper; // 引入新搬家的 Mapper
import com.example.campushelp.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminMapper adminMapper;

    // 1. 获取所有用户
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    // 2. 获取所有订单
    @GetMapping("/orders")
    public List<Map<String, Object>> getAllOrders() {
        return adminMapper.findAllOrders();
    }
}