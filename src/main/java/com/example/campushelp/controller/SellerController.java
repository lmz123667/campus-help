package com.example.campushelp.controller;

import com.example.campushelp.entity.Availability;
import com.example.campushelp.mapper.AvailabilityMapper;
import com.example.campushelp.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private AvailabilityMapper availabilityMapper;

    @Autowired
    private OrderMapper orderMapper; // 【新增】注入 OrderMapper

    // 发布接口
    @PostMapping("/publish")
    public Map<String, Object> publish(@RequestBody Availability availability) {
        Map<String, Object> result = new HashMap<>();
        try {
            availabilityMapper.insert(availability);
            result.put("success", true);
            result.put("message", "发布成功！等待买家联系");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发布失败: " + e.getMessage());
        }
        return result;
    }

    // 查询我的发布列表
    @GetMapping("/my-list")
    public List<Availability> getMyList(@RequestParam Integer sellerId) {
        return availabilityMapper.findBySellerId(sellerId);
    }

    // 【新增】查询我收到的订单 (用于联系买家)
    @GetMapping("/orders")
    public List<Map<String, Object>> getMyOrders(@RequestParam Integer sellerId) {
        return orderMapper.findBySellerId(sellerId);
    }

    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestBody Map<String, Integer> params) {
        Integer id = params.get("id");

        // 简单直接删除
        // (注：如果已经被预订了，数据库如果有外键约束可能会报错，或者我们可以前端限制只能删空闲的)
        availabilityMapper.deleteById(id);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "删除成功");
        return result;
    }
}