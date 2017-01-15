package com.share.multikeys;

import com.share.multikeys.entity.Item;
import com.share.multikeys.entity.Order;
import com.share.multikeys.service.ItemService;
import com.share.multikeys.service.OrderService;
import com.share.multikeys.utils.DateUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class Main {
    @SuppressWarnings("unused")
	public static void main(final String[] args) {
        @SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("META-INF/multiMybatisContext.xml");
          OrderService orderService = applicationContext.getBean(OrderService.class);
        Order criteria = new Order();
        criteria.setUserId(10);
        criteria.setOrderId(10);
        criteria.setStatus("INSERT_TEST_12");
        criteria.setCreateTime(DateUtil.stringToDate("2016-10-08 17:08:38", "yyyy-MM-dd HH:mm:ss"));
        //orderService.insert(criteria);
        orderService.selectByKey(1);
        orderService.selectByKeyOrTime(1,DateUtil.stringToDate("2016-10-08 17:08:38", "yyyy-MM-dd HH:mm:ss"));
        orderService.selectBetweenTime(DateUtil.addDays(new Date(), -30),new Date());
        orderService.selectRangeTime(DateUtil.addDays(new Date(), -30),new Date());
          
          ItemService itemService = applicationContext.getBean(ItemService.class);
        Item item=new Item();
        item.setItemId(15);
        item.setOrderId(1);
        item.setItemName("testName");
        itemService.insert(item);
        itemService.selectJoin(1);
    }
}
