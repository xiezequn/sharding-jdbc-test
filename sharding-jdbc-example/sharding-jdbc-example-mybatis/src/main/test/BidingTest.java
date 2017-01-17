import com.dangdang.ddframe.rdb.sharding.example.jdbc.entity.OrderItem;
import com.dangdang.ddframe.rdb.sharding.example.jdbc.repository.OrderItemMapper;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiezq
 * Date: 2017/1/13
 * Depiction:绑定表测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/mybatisBidingContext.xml"})
public class BidingTest {

    @Autowired
    private OrderItemMapper orderItemMapper;

    //com.dangdang.ddframe.rdb.sharding.router.SQLRouteEngine.routeSQL(com.dangdang.ddframe.rdb.sharding.parser.result.SQLParsedResult, java.util.List<java.lang.Object>)

    /**
     <rdb:binding-table-rules>
     <rdb:binding-table-rule logic-tables="t_order, t_order_item"/>
     </rdb:binding-table-rules>
     */
    @Test
    public void binding(){
        List<OrderItem>  list =orderItemMapper.binding(1000L,10);
        System.out.println("size:"+list.size()+"："+list);
    }

    @Test
    public void selectLeftJoin(){
        List<OrderItem>  list =orderItemMapper.selectLeftJoin(100001L);
        System.out.println("size:"+list.size()+"："+list);
    }

}
