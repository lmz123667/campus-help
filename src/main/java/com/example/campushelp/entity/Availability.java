package com.example.campushelp.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Availability {
    private Integer id;
    private Integer sellerId;      // 谁发布的
    private LocalDate availableDate; // 哪天有空
    private String timeSlot;       // 什么时间段 (例如 "14:00-16:00")
    private Integer status;        // 0=空闲, 1=已预订
}