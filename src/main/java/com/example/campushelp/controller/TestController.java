package com.example.campushelp.controller;

import com.example.campushelp.entity.User;
import com.example.campushelp.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private UserMapper userMapper;

    // 浏览器访问 http://localhost:8080/test 就能看到结果
    @GetMapping("/test")
    public List<User> testDatabase() {
        return userMapper.findAll();
    }
}