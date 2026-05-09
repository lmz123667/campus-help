package com.example.campushelp;

import org.mybatis.spring.annotation.MapperScan; // 1. 务必添加这个导入
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 2. 添加下面这行注解，括号里是你的 Mapper 接口所在的包路径
@MapperScan("com.example.campushelp.mapper")
public class CampusHelpApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusHelpApplication.class, args);
    }

}
