import com.dangdang.ddframe.rdb.sharding.example.jdbc.entity.Order;
import com.dangdang.ddframe.rdb.sharding.example.jdbc.repository.OrderRepository;
import com.dangdang.ddframe.rdb.sharding.example.jdbc.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: xiezq
 * Date: 2017/1/11
 * Depiction:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/mybatisDynamicContext.xml"})
//动态表测试
public class DynamicTest {

    @Autowired
    private OrderService orderService;

    @Resource
    private OrderRepository orderMapper;

    @Test
    public void test(){
        Order order=new Order();
        order.setOrderId(5001);
        order.setStatus("test");
        order.setUserId(1);
        /**
         CREATE TABLE IF NOT EXISTS `ds_1`.`t_order_5` (`order_id` BIGINT NOT NULL, `user_id` INT NOT NULL, `status` VARCHAR(50), PRIMARY KEY (`order_id`));
         SELECT * FROM `ds_1`.`t_order_5`;
         DROP TABLE `ds_1`.`t_order_5`;
         */
        orderService.insert(order);
    }

    @Test
    public void selectById(){
        //SELECT * FROM `ds_1`.`t_order_5` where order_id=5001
        System.out.println("selectById:"+orderMapper.selectById(5001L,1));
    }


}
