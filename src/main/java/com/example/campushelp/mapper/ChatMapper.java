package com.example.campushelp.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChatMapper {

    // 保存消息
    @Insert("INSERT INTO chat_messages(sender_id, receiver_id, content, send_time) " +
            "VALUES(#{senderId}, #{receiverId}, #{content}, NOW())")
    void insert(Integer senderId, Integer receiverId, String content);

    // 查询两人之间的聊天历史 (按时间排序)
    @Select("SELECT * FROM chat_messages " +
            "WHERE (sender_id = #{userId1} AND receiver_id = #{userId2}) " +
            "   OR (sender_id = #{userId2} AND receiver_id = #{userId1}) " +
            "ORDER BY send_time ASC")
    List<Map<String, Object>> findHistory(Integer userId1, Integer userId2);
}