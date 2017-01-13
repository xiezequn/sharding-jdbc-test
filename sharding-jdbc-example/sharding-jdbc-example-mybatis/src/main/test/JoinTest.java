import com.dangdang.ddframe.rdb.sharding.example.jdbc.repository.OrderItemMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: xiezq
 * Depiction:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/mybatisContext.xml"})
public class JoinTest {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Test
    public void selectJoin(){
        orderItemMapper.selectJoin(100001L);
    }

    //com.dangdang.ddframe.rdb.sharding.router.SQLRouteEngine.routeSQL(com.dangdang.ddframe.rdb.sharding.parser.result.SQLParsedResult, java.util.List<java.lang.Object>)
    @Test//左连接，查询结果不符
    public void selectLeftJoin(){
        orderItemMapper.selectLeftJoin(100001L);
    }
    /**
     ds_0:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_1 i LEFT JOIN t_order_1 o ON i.order_id = o.order_id WHERE item_id = ? AND o.order_id IS NOT NULL
     ds_1:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_0 i LEFT JOIN t_order_1 o ON i.order_id = o.order_id WHERE item_id = ? AND o.order_id IS NOT NULL
     ds_1:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_1 i LEFT JOIN t_order_0 o ON i.order_id = o.order_id WHERE item_id = ? AND o.order_id IS NOT NULL
     ds_1:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_0 i LEFT JOIN t_order_0 o ON i.order_id = o.order_id WHERE item_id = ? AND o.order_id IS NOT NULL
     ds_0:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_0 i LEFT JOIN t_order_1 o ON i.order_id = o.order_id WHERE item_id = ? AND o.order_id IS NOT NULL
     ds_0:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_0 i LEFT JOIN t_order_0 o ON i.order_id = o.order_id WHERE item_id = ? AND o.order_id IS NOT NULL
     ds_1:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_1 i LEFT JOIN t_order_1 o ON i.order_id = o.order_id WHERE item_id = ? AND o.order_id IS NOT NULL
     ds_0:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_1 i LEFT JOIN t_order_0 o ON i.order_id = o.order_id WHERE item_id = ? AND o.order_id IS NOT NULL
     */

}
