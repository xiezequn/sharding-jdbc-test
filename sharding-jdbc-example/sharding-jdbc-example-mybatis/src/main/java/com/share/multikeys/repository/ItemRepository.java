package com.share.multikeys.repository;

import com.share.multikeys.entity.Item;
import com.share.multikeys.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 
* @author weigen.ye 
* @date 创建时间：2016年10月8日 下午5:45:14 
*
*/               
public interface ItemRepository {

	public Item selectJoin(@Param(value = "orderId") Integer orderId);
	
	void insert(Item model);
    
    int update(List<Integer> userIds);
    
    List<Order> selectAll();
    
    List<Order> selectByKey(@Param(value = "itemId") Integer itemId);
}
