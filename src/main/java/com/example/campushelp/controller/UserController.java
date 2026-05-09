package com.example.campushelp.controller;

import com.example.campushelp.entity.User;
import com.example.campushelp.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    // 获取最新用户信息 (刷新信誉分、余额等)
    @GetMapping("/info")
    public User getUserInfo(@RequestParam Integer id) {
        return userMapper.findById(id);
    }
}