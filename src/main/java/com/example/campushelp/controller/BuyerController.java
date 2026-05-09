package com.example.campushelp.controller;

import com.example.campushelp.mapper.AvailabilityMapper;
import com.example.campushelp.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer")
public class BuyerController {

    @Autowired
    private AvailabilityMapper availabilityMapper;

    @Autowired
    private OrderMapper orderMapper; // 【新增】注入 OrderMapper

    // 搜索接口
    @GetMapping("/search")
    public List<Map<String, Object>> search(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return availabilityMapper.findByDate(localDate);
    }

    // 【新增】买家获取自己的订单列表
    @GetMapping("/my-orders")
    public List<Map<String, Object>> getMyOrders(@RequestParam Integer buyerId) {
        return orderMapper.findByBuyerId(buyerId);
    }
}