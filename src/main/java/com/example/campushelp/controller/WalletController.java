package com.example.campushelp.controller;

import com.example.campushelp.entity.User;
import com.example.campushelp.mapper.UserMapper;
import com.example.campushelp.mapper.WithdrawMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WithdrawMapper withdrawMapper;

    // 1. 查询余额
    @GetMapping("/balance")
    public BigDecimal getBalance(@RequestParam Integer userId) {
        User user = userMapper.findById(userId);
        return user.getBalance() == null ? BigDecimal.ZERO : user.getBalance();
    }

    // 2. 模拟充值 (买家点一下直接加钱)
    @PostMapping("/recharge")
    public Map<String, Object> recharge(@RequestBody Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        BigDecimal amount = new BigDecimal(params.get("amount").toString());

        userMapper.updateBalance(userId, amount);

        return Map.of("success", true, "message", "充值成功！");
    }

    // 3. 申请提现 (卖家)
    @PostMapping("/withdraw")
    @Transactional
    public Map<String, Object> withdraw(@RequestBody Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        BigDecimal amount = new BigDecimal(params.get("amount").toString());

        User user = userMapper.findById(userId);
        if (user.getBalance().compareTo(amount) < 0) {
            return Map.of("success", false, "message", "余额不足！");
        }

        // 1. 先扣除余额
        userMapper.updateBalance(userId, amount.negate());
        // 2. 生成提现申请单
        withdrawMapper.insert(userId, amount);

        return Map.of("success", true, "message", "申请已提交，等待管理员审核");
    }

    // 4. 管理员获取提现列表
    @GetMapping("/admin/withdraws")
    public List<Map<String, Object>> getAllWithdraws() {
        return withdrawMapper.findAll();
    }

    // 5. 卖家获取自己的提现列表
    @GetMapping("/my-withdraws")
    public List<Map<String, Object>> getMyWithdraws(@RequestParam Integer userId) {
        return withdrawMapper.findByUserId(userId);
    }

    // 6. 管理员审核提现
    @PostMapping("/admin/audit")
    @Transactional
    public Map<String, Object> auditWithdraw(@RequestBody Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        Integer status = (Integer) params.get("status"); // 1-通过, 2-驳回

        if (status == 2) {
            // 如果驳回，要把钱退回给卖家
            Map<String, Object> withdraw = withdrawMapper.findById(id);
            Integer userId = (Integer) withdraw.get("user_id");
            BigDecimal amount = (BigDecimal) withdraw.get("amount");
            userMapper.updateBalance(userId, amount);
        }

        withdrawMapper.updateStatus(id, status);
        return Map.of("success", true, "message", "操作成功");
    }
}