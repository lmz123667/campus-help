package com.example.campushelp.controller;

import com.example.campushelp.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    @Autowired
    private ChatMapper chatMapper;

    @GetMapping("/chat/history")
    public List<Map<String, Object>> getHistory(@RequestParam Integer userId1, @RequestParam Integer userId2) {
        return chatMapper.findHistory(userId1, userId2);
    }
}