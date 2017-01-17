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

package com.dangdang.ddframe.rdb.sharding.example.jdbc.service;

import com.dangdang.ddframe.rdb.sharding.example.jdbc.entity.Order;
import com.dangdang.ddframe.rdb.sharding.example.jdbc.repository.OrderRepository;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Order 服务对象.
 * 
 * @author gaohongtao
 */
@Service
@Transactional
public class OrderService {
    
    @Resource
    private OrderRepository orderRepository;
    
    @Transactional(readOnly = true)
    public List<Order> selectAll() {
        List<Order> list =orderRepository.selectAll();
        System.out.println(list.size()+":"+list);
        return list;
    }
    
    public void clear() {
        orderRepository.deleteAll();
    }
    
    public void fooService() {
        Order criteria = new Order();
        criteria.setOrderId(1);
        criteria.setStatus("INSERT");
        orderRepository.insert(criteria);
        System.out.println(criteria.getOrderId());
        criteria.setUserId(11);
        criteria.setStatus("INSERT2");
        orderRepository.insert(criteria);
        System.out.println(criteria.getOrderId());
        orderRepository.update(Lists.newArrayList(10, 11));
    }
    
    public void fooServiceWithFailure() {
        selectAll();
        Order criteria = new Order();
        criteria.setOrderId(1);
        criteria.setStatus("INSERT");
        orderRepository.insert(criteria);
        //System.out.println(criteria.getOrderId());
        criteria.setUserId(11);
        criteria.setStatus("INSERT2");
        orderRepository.insert(criteria);
        //System.out.println(criteria.getOrderId());
        orderRepository.update(Lists.newArrayList(10, 11));
        selectAll();
        clear();
        selectAll();
        throw new IllegalArgumentException("failed");
    }

    public void insert(Order order){
        orderRepository.insertWithId(order);
    }

    public void update(){
        Order order=orderRepository.selectById(1000L,10);
        System.out.println(order);
        order.setStatus("test123");
        orderRepository.updateByPrimaryKey(order);
        orderRepository.selectById(1000L,10);
        System.out.println(order);
    }
}
