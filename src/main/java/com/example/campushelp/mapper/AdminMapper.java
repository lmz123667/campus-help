package com.example.campushelp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {

    // 查询所有订单，关联查出买家和卖家的名字
    @Select("SELECT o.*, b.nickname as buyerName, s.nickname as sellerName " +
            "FROM orders o " +
            "LEFT JOIN users b ON o.buyer_id = b.id " +
            "LEFT JOIN users s ON o.seller_id = s.id " +
            "ORDER BY o.create_time DESC")
    List<Map<String, Object>> findAllOrders();
}