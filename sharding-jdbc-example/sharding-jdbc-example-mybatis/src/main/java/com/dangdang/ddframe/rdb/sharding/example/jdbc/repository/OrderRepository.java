/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.rdb.sharding.example.jdbc.repository;

import java.util.List;

import com.dangdang.ddframe.rdb.sharding.example.jdbc.entity.Function;
import com.dangdang.ddframe.rdb.sharding.example.jdbc.entity.GroupBy;
import com.dangdang.ddframe.rdb.sharding.example.jdbc.entity.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderRepository {
    
    void insert(Order model);

    void insertWithId(Order model);

    void insertBatch(List<Order> list);
    
    int update(List<Integer> userIds);

    void updateByPrimaryKey(Order order);
    
    int deleteAll();
    
    List<Order> selectAll();

    Order selectById(@Param("orderId") Long orderId,@Param("userId")Integer userId);

    List<Order> selectEqueal (int userId);

    List<Order> selectGreater(int userId);

    List<Order> selectIn(List orderIds);

    List<Order> selectBeteen(@Param("minOrderId") long minOrderId, @Param("maxOrderId")long maxOrderId);

    List<Order> selectLimit(@Param("index") int index,@Param("pageSizes")int pageSizes);

    Function function();

    List<GroupBy> groupBy();
}
