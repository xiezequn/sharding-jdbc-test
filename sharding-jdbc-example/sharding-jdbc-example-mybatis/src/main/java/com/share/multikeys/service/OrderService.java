package com.share.multikeys.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.share.multikeys.entity.Order;
import com.share.multikeys.repository.OrderRepository;

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
    public void select() {
        System.out.println(orderRepository.selectAll());
    }
    
    public void selectByKey(Integer orderId) {
    	Long s=System.currentTimeMillis();
        System.out.println("cost "+(System.currentTimeMillis()-s)/1000+"S: "+orderRepository.selectByKey(orderId));
    }
    
    public void selectByKeyOrTime(Integer orderId,Date date) {
    	Long s=System.currentTimeMillis();
        System.out.println("cost "+(System.currentTimeMillis()-s)/1000+"S: "+orderRepository.selectByKeyOrTime(orderId,date));
    }
    
    
    public void selectBetweenTime(Date sDate,Date eDate) {
    	Long s=System.currentTimeMillis();
        System.out.println("cost "+(System.currentTimeMillis()-s)/1000+"S: "+orderRepository.selectBetweenTime(sDate,eDate));
    }
    
    public void selectRangeTime(Date sDate,Date eDate) {
    	Long s=System.currentTimeMillis();
        System.out.println("cost "+(System.currentTimeMillis()-s)/1000+"S: "+orderRepository.selectRangeTime(sDate,eDate));
    }
    
    public void selectInRange(){
    	
    }
    
    public void insert(Order order) {
    	orderRepository.insert(order);
	}
    public void insert() {
    	 Order criteria = new Order();
         criteria.setUserId(10);
         //criteria.setOrderId(1012);
         criteria.setStatus("INSERT_TEST_12");
         criteria.setCreateTime(new Date());
         orderRepository.insert(criteria);
    }
    
}
