package com.example.campushelp.mapper;

import com.example.campushelp.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users")
    List<User> findAll();

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Integer id);

    @Insert("INSERT INTO users(username, password, nickname, role, contact_info) " +
            "VALUES(#{username}, #{password}, #{nickname}, #{role}, #{contactInfo})")
    int insert(User user);

    // 【新增】更新余额 (amount 可以是正数也可以是负数)
    @Update("UPDATE users SET balance = balance + #{amount} WHERE id = #{id}")
    void updateBalance(Integer id, BigDecimal amount);

    // 【新增】更新信誉分 (points 可以是正数或负数)
    @Update("UPDATE users SET credit_score = credit_score + #{points} WHERE id = #{userId}")
    void updateCreditScore(Integer userId, Integer points);
}