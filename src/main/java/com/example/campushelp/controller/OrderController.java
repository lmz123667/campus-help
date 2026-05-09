package com.example.campushelp.controller;

import com.example.campushelp.entity.Order;
import com.example.campushelp.entity.User;
import com.example.campushelp.mapper.OrderMapper;
import com.example.campushelp.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    // 平台抽成比例 (10%)
    private static final BigDecimal FEE_RATE = new BigDecimal("0.10");

    // 下单接口 (同时扣款)
    @PostMapping("/create")
    @Transactional
    public Map<String, Object> createOrder(@RequestBody Order order) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1. 检查买家余额
            User buyer = userMapper.findById(order.getBuyerId());
            if (buyer.getBalance() == null || buyer.getBalance().compareTo(order.getPrice()) < 0) {
                result.put("success", false);
                result.put("message", "余额不足，请先充值！");
                return result;
            }

            // 2. 扣除买家余额 (资金进入平台担保池)
            userMapper.updateBalance(order.getBuyerId(), order.getPrice().negate());

            // 3. 生成订单并锁定时间
            order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
            orderMapper.insert(order);
            orderMapper.lockAvailability(order.getAvailabilityId());

            result.put("success", true);
            result.put("message", "支付成功，订单已生成！");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "下单失败：" + e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    // 更新订单状态 (含退款/打款逻辑)
    @PostMapping("/updateStatus")
    @Transactional
    public Map<String, Object> updateStatus(@RequestBody Map<String, Integer> params) {
        Integer id = params.get("id");
        Integer status = params.get("status");

        // 先查出订单详情，因为要用到价格和人员ID
        Order order = orderMapper.findById(id);
        // 注意：你需要去 OrderMapper 加一个 findById 方法，
        // 或者简单点，我们假设前端传过来了 price, buyerId, sellerId。
        // 为了安全，建议去 OrderMapper 加一个 findById。这里为了演示，我先手动补全逻辑。

        // === 核心资金处理 ===
        if (status == 2) {
            // 【确认完成】：平台扣除抽成，打款给卖家
            BigDecimal total = order.getPrice();
            BigDecimal fee = total.multiply(FEE_RATE); // 抽成
            BigDecimal income = total.subtract(fee);   // 卖家实际收入

            // 卖家加钱
            userMapper.updateBalance(order.getSellerId(), income);

        } else if (status == 3) {
            // 【取消订单】：全额退款给买家
            userMapper.updateBalance(order.getBuyerId(), order.getPrice());
        }

        orderMapper.updateStatus(id, status);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "操作成功");
        return result;
    }

    @PostMapping("/rate")
    @Transactional
    public Map<String, Object> rateOrder(@RequestBody Map<String, Integer> params) {
        Integer orderId = params.get("orderId");
        Integer score = params.get("score"); // 1~5 分

        // 1. 获取订单信息
        Order order = orderMapper.findById(orderId);
        if (order.getIsRated() == 1) {
            return Map.of("success", false, "message", "该订单已评价，请勿重复提交");
        }

        // 2. 给卖家加分 (逻辑：1星扣分，5星加分，或者简单点直接加分)
        // 这里采用简单逻辑：直接把星星数加到信誉分里 (比如 5星就+5分)
        userMapper.updateCreditScore(order.getSellerId(), score);

        // 3. 标记订单为已评价
        orderMapper.markAsRated(orderId);

        return Map.of("success", true, "message", "评价成功，感谢您的反馈！");
    }
}