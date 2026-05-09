package com.example.campushelp.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal; // 1. 务必引入这个包

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String role;        // ADMIN, BUYER, SELLER
    private String contactInfo;
    private LocalDateTime createdAt;

    // 2. 【新增】钱包余额 (对应数据库的 balance 字段)
    private BigDecimal balance;

    // 3. 【新增】信誉分 (对应之前加的 credit_score 字段)
    private Integer creditScore;
}