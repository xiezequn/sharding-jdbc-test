package com.dangdang.ddframe.rdb.sharding.example.jdbc.repository;


import com.dangdang.ddframe.rdb.sharding.example.jdbc.entity.OrderItem;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Long itemId);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Long itemId);

    List<OrderItem> selectList(Long itemId);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);


}