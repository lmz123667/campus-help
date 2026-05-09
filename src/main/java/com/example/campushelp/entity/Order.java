package com.example.campushelp.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    private Integer id;
    private String orderNo;
    private Integer buyerId;
    private Integer sellerId;
    private Integer availabilityId;
    private String courseName;
    private String classroom;
    private BigDecimal price;
    private Integer status;
    private LocalDateTime createTime;

    // 【新增】是否已评价 (0:否, 1:是)
    private Integer isRated;
}