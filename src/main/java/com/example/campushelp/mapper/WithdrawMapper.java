package com.example.campushelp.mapper;

import org.apache.ibatis.annotations.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface WithdrawMapper {

    // 申请提现
    @Insert("INSERT INTO withdraws(user_id, amount, status) VALUES(#{userId}, #{amount}, 0)")
    void insert(Integer userId, BigDecimal amount);

    // 管理员查询所有提现申请
    @Select("SELECT w.*, u.nickname, u.username, u.contact_info " +
            "FROM withdraws w " +
            "LEFT JOIN users u ON w.user_id = u.id " +
            "ORDER BY w.create_time DESC")
    List<Map<String, Object>> findAll();

    // 卖家查询自己的提现记录
    @Select("SELECT * FROM withdraws WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Map<String, Object>> findByUserId(Integer userId);

    // 审核提现 (修改状态)
    @Update("UPDATE withdraws SET status = #{status} WHERE id = #{id}")
    void updateStatus(Integer id, Integer status);

    // 根据ID查询申请 (用于审核时获取金额)
    @Select("SELECT * FROM withdraws WHERE id = #{id}")
    Map<String, Object> findById(Integer id);
}