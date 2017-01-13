import com.dangdang.ddframe.rdb.sharding.api.HintManager;
import com.dangdang.ddframe.rdb.sharding.example.jdbc.entity.Order;
import com.dangdang.ddframe.rdb.sharding.example.jdbc.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: xiezq
 * Depiction:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/mybatisContext.xml"})
public class CrudTest {

    @Autowired
    private OrderRepository orderMapper;

    @Test
    // insert
    //自增长ID
    //id-generator-class="com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator"
    //<rdb:auto-increment-column column-name="order_id"/>
    // keyProperty="orderId" useGeneratedKeys="true"
    public void insert(){
        for(int i=1;i<20;i++){
            Order order=new Order();
            order.setStatus("test insert");
            order.setUserId(i);
            orderMapper.insert(order);
        }
    }

    @Test
    public void insertBatch(){
        List<Order> orders=new ArrayList();
        for(int i=21;i<=40;i++){
            Order order=new Order();
            order.setStatus("test insert");
            order.setUserId(new Random().nextInt(20)+1);
            order.setOrderId(i);
            orders.add(order);
        }
        int databaseCount=2;
        int tableCount=2;
        Map<String,List<Order>> map =new HashMap();
        for(Order order:orders){
            int database=order.getUserId()%databaseCount;
            long table=order.getOrderId()%tableCount;
            if(map.get(database+"-"+table)==null){
                map.put(database+"-"+table,new ArrayList<Order>());
            }
            map.get(database+"-"+table).add(order);
        }
        for (String key : map.keySet()) {
            orderMapper.insertBatch(map.get(key));
        }

    }

    @Test
    public void update(){
        orderMapper.update(Arrays.asList(1,2,3,4,5));
    }

    @Test
    public void deleteAll(){
        orderMapper.deleteAll();
    }


    @Test
    // order by
    public void selectAll(){
        orderMapper.selectAll();
    }

    @Test
    //基于暗示(Hint)的分片键值管理器
    public void hint(){
        List<Order> list =orderMapper.selectAll();
        System.out.println(list.size());
        HintManager hintManager = HintManager.getInstance();
        //添加数据源分片键值
        hintManager.addDatabaseShardingValue("t_order", "user_id", 10);
        //添加表分片键值
        hintManager.addTableShardingValue("t_order", "order_id", 1L);
        list =orderMapper.selectAll();
        System.out.println(list.size());
        //清除ThreadLocal
        hintManager.close();
        list =orderMapper.selectAll();
        System.out.println(list.size());
    }

    @Test //  =
    public void selectEqueal(){
        orderMapper.selectEqueal(5);
    }

    @Test //  >
    public void selectGreater(){
        orderMapper.selectGreater(5);
    }

    @Test //  in
    public void selectIn(){
        orderMapper.selectIn(Arrays.asList(1,2,3,4,5));
    }

    @Test //  beteen
    public void selectBeteen(){
        orderMapper.selectBeteen(1,5);
    }


    @Test
    //order by ,limit
    public void selectLimit(){
        orderMapper.selectLimit();
    }


    @Test
    //function
    public void function(){
        orderMapper.function();
    }

    /*
    SELECT user_id,COUNT(1) FROM (
            SELECT * FROM `ds_0`.`t_order_0` UNION ALL SELECT * FROM `ds_0`.`t_order_1`
            UNION ALL SELECT * FROM `ds_1`.`t_order_0` UNION ALL SELECT * FROM `ds_1`.`t_order_1`)
    t_order  GROUP BY user_id
    */
    @Test
    //groupBy
    public void groupBy(){
        orderMapper.groupBy();
    }


}
