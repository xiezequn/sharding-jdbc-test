import com.dangdang.ddframe.rdb.sharding.api.HintManager;
import com.dangdang.ddframe.rdb.sharding.example.jdbc.entity.Order;
import com.dangdang.ddframe.rdb.sharding.example.jdbc.repository.OrderRepository;
import com.dangdang.ddframe.rdb.sharding.example.jdbc.service.OrderService;
import com.dangdang.ddframe.rdb.sharding.jdbc.MasterSlaveDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: xiezq
 * Date: 2017/1/17
 * Depiction:主从测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/masterSlaveMybatisContext.xml"})
public class MasterSlaveTest {

    //判断主从规则 MasterSlaveDataSourceTest
    //com.dangdang.ddframe.rdb.sharding.jdbc.MasterSlaveDataSource.getDataSource()
    //com.dangdang.ddframe.rdb.sharding.jdbc.MasterSlaveDataSource.isDML()
    /**使用master库规则
     1.SQLStatementType.SELECT != sqlStatementType
     2.之前有执行update insert
     3.HintManager hintManager = HintManager.getInstance();
     hintManager.setMasterRouteOnly();
     //执行数据库操作
     hintManager.close();
     重置标记
     MasterSlaveDataSource.resetDMLFlag();
     */

    @Autowired
    private OrderRepository orderMapper;
    @Autowired
    OrderService orderService;

    @Test//select com.dangdang.ddframe.rdb.sharding.jdbc.MasterSlaveDataSource.getDataSource() 返回从库数据源
    public void select(){
        System.out.println(orderMapper.selectAll());
    }

    @Test//select com.dangdang.ddframe.rdb.sharding.jdbc.MasterSlaveDataSource.getDataSource() 返回主库数据源
    public void insert(){
        Order order=new Order();
        long orderId=2000+new Random().nextInt(2000);
        order.setOrderId(orderId);
        order.setUserId(2);
        order.setStatus("status test");
        orderMapper.insert(order);
    }

    @Test
    public void update(){
        orderService.update();

    }

    @Test//使用hint切换到主库
    public void hint(){
        HintManager hintManager = HintManager.getInstance();
        hintManager.setMasterRouteOnly();
        //执行数据库操作
        Order order=orderMapper.selectById(1000L,10);
        hintManager.close();
        //重置标记
        MasterSlaveDataSource.resetDMLFlag();
    }



}
