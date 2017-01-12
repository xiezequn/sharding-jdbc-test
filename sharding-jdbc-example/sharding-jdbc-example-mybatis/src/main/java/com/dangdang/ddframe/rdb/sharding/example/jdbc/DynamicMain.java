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

package com.dangdang.ddframe.rdb.sharding.example.jdbc;

import com.dangdang.ddframe.rdb.sharding.example.jdbc.entity.Order;
import com.dangdang.ddframe.rdb.sharding.example.jdbc.service.OrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// CHECKSTYLE:OFF
@Service
@Transactional
//动态表测试
public class DynamicMain {
    public static void main(final String[] args) {
        // CHECKSTYLE:ON
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("META-INF/mybatisDynamicContext.xml");
        OrderService orderService = applicationContext.getBean(OrderService.class);
        Order order=new Order();
        order.setOrderId(5);
        order.setStatus("test");
        order.setUserId(1);
        /**
         CREATE TABLE IF NOT EXISTS `ds_1`.`t_order_5` (`order_id` BIGINT NOT NULL, `user_id` INT NOT NULL, `status` VARCHAR(50), PRIMARY KEY (`order_id`));
         SELECT * FROM `ds_1`.`t_order_5`;
         DROP TABLE `ds_1`.`t_order_5`;
         */
        orderService.insert(order);

    }
}
