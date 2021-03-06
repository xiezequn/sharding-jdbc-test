package com.dangdang.ddframe.rdb.sharding.example.jdbc.repository;


import com.dangdang.ddframe.rdb.sharding.example.jdbc.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Long itemId);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Long itemId);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> selectJoin(Long itemId);

    List<OrderItem> selectLeftJoin(Long itemId);

    List<OrderItem> binding(@Param("orderId") Long orderId,@Param("userId") Integer userId);




}