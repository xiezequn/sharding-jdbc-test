
package com.share.multikeys.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.share.multikeys.entity.Order;

public interface OrderRepository {
    
    void insert(Order model);
    
    int update(List<Integer> userIds);
    
    List<Order> selectAll();
    
    List<Order> selectByKey(Integer orderId);
    
    List<Order> selectByKeyOrTime(@Param("orderId") Integer orderId, @Param("date") Date date);
    
    List<Order> selectBetweenTime(@Param("sDate") Date sDate, @Param("eDate") Date eDate);
    
    List<Order> selectRangeTime(@Param("sDate") Date sDate, @Param("eDate") Date eDate);
    
    List<Order> selectBetweenKey(@Param("sId") Integer sId, @Param("eId") Integer eId);
}
