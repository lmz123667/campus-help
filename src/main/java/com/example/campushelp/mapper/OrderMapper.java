package com.example.campushelp.mapper;

import com.example.campushelp.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    // 1. 创建订单
    @Insert("INSERT INTO orders(order_no, buyer_id, seller_id, availability_id, course_name, classroom, price, status) " +
            "VALUES(#{orderNo}, #{buyerId}, #{sellerId}, #{availabilityId}, #{courseName}, #{classroom}, #{price}, 0)")
    int insert(Order order);

    // 2. 锁定时间段
    @Update("UPDATE availabilities SET status = 1 WHERE id = #{id}")
    void lockAvailability(Integer id);

    // 3. 修改订单状态
    @Update("UPDATE orders SET status = #{status} WHERE id = #{id}")
    int updateStatus(Integer id, Integer status);

    // 4. 卖家查询收到的订单
    @Select("SELECT o.*, u.nickname as buyerName, u.id as buyerId " +
            "FROM orders o " +
            "LEFT JOIN users u ON o.buyer_id = u.id " +
            "WHERE o.seller_id = #{sellerId} " +
            "ORDER BY o.create_time DESC")
    List<Map<String, Object>> findBySellerId(Integer sellerId);

    // 5. 【关键修改】买家查询自己的订单
    // 增加了 'o.is_rated as isRated' 别名，确保前端能读取驼峰命名的变量
    @Select("SELECT o.*, o.is_rated as isRated, u.nickname as sellerName, u.id as sellerId " +
            "FROM orders o " +
            "LEFT JOIN users u ON o.seller_id = u.id " +
            "WHERE o.buyer_id = #{buyerId} " +
            "ORDER BY o.create_time DESC")
    List<Map<String, Object>> findByBuyerId(Integer buyerId);

    // 6. 标记订单为已评价
    @Update("UPDATE orders SET is_rated = 1 WHERE id = #{id}")
    void markAsRated(Integer id);

    // 7. 根据ID查询订单详情 (用于资金结算等)
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Order findById(Integer id);
}