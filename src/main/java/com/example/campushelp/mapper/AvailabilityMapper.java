package com.example.campushelp.mapper;

import com.example.campushelp.entity.Availability;
import org.apache.ibatis.annotations.Delete; // 👈 关键：补上了这个导入
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface AvailabilityMapper {

    // 1. 发布空闲时间
    @Insert("INSERT INTO availabilities(seller_id, available_date, time_slot, status) " +
            "VALUES(#{sellerId}, #{availableDate}, #{timeSlot}, 0)")
    int insert(Availability availability);

    // 2. 查询某个卖家发布过的所有记录
    @Select("SELECT * FROM availabilities WHERE seller_id = #{sellerId} ORDER BY available_date DESC")
    List<Availability> findBySellerId(Integer sellerId);

    // 3. (给买家用的) 查询所有空闲的记录
    @Select("SELECT * FROM availabilities WHERE status = 0 ORDER BY available_date ASC")
    List<Availability> findAllAvailable();

    // 4. 根据日期搜索空闲的卖家 (带买家信息)
    @Select("SELECT a.*, u.nickname as sellerName, u.contact_info as sellerContact, u.credit_score as sellerScore " +
            "FROM availabilities a " +
            "LEFT JOIN users u ON a.seller_id = u.id " +
            "WHERE a.status = 0 AND a.available_date = #{date} " +
            "ORDER BY u.credit_score DESC")
    List<Map<String, Object>> findByDate(LocalDate date);

    // 5. 【新增】根据ID删除发布记录 (用于卖家删除)
    @Delete("DELETE FROM availabilities WHERE id = #{id}")
    int deleteById(Integer id);
}