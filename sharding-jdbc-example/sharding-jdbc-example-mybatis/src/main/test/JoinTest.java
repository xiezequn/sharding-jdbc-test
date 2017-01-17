import com.dangdang.ddframe.rdb.sharding.example.jdbc.entity.OrderItem;
import com.dangdang.ddframe.rdb.sharding.example.jdbc.repository.OrderItemMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

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
        List<OrderItem> list =orderItemMapper.selectJoin(100001L);
        System.out.println("size:"+list.size()+"："+list);
    }

    //com.dangdang.ddframe.rdb.sharding.router.SQLRouteEngine.routeSQL(com.dangdang.ddframe.rdb.sharding.parser.result.SQLParsedResult, java.util.List<java.lang.Object>)
    @Test//左连接，查询结果不符
    public void selectLeftJoin(){
        List<OrderItem>  list =orderItemMapper.selectLeftJoin(100001L);
        System.out.println("size:"+list.size()+"："+list);
    }
    @Test
    public void binding(){
        List<OrderItem>  list =orderItemMapper.binding(1000L,10);
        System.out.println("size:"+list.size()+"："+list);
    }
    /**
     select i.item_id, i.order_id, i.user_id,o.status order_status from t_order_item i
     left JOIN t_order o on i.order_id =o.order_id where item_id = #{itemId,jdbcType=BIGINT}

     见com.dangdang.ddframe.rdb.sharding.router.SQLRouteEngine.routeSQL(com.dangdang.ddframe.rdb.sharding.parser.result.SQLParsedResult, java.util.List<java.lang.Object>)
     SQL会改写成：

     ds_0:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_1 i LEFT JOIN t_order_1 o ON i.order_id = o.order_id WHERE item_id = ? 
     ds_1:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_0 i LEFT JOIN t_order_1 o ON i.order_id = o.order_id WHERE item_id = ? 
     ds_1:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_1 i LEFT JOIN t_order_0 o ON i.order_id = o.order_id WHERE item_id = ? 
     ds_1:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_0 i LEFT JOIN t_order_0 o ON i.order_id = o.order_id WHERE item_id = ? 
     ds_0:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_0 i LEFT JOIN t_order_1 o ON i.order_id = o.order_id WHERE item_id = ? 
     ds_0:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_0 i LEFT JOIN t_order_0 o ON i.order_id = o.order_id WHERE item_id = ? 
     ds_1:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_1 i LEFT JOIN t_order_1 o ON i.order_id = o.order_id WHERE item_id = ? 
     ds_0:SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_1 i LEFT JOIN t_order_0 o ON i.order_id = o.order_id WHERE item_id = ?

     如果绑定表
     ds_0：SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_0 i LEFT JOIN t_order_0 o ON i.order_id = o.order_id WHERE item_id = ?
     ds_0：sql=SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_1 i LEFT JOIN t_order_1 o ON i.order_id = o.order_id WHERE item_id = ?
     ds_1：sql=SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_1 i LEFT JOIN t_order_1 o ON i.order_id = o.order_id WHERE item_id = ?
     ds_1：sql=SELECT i.item_id, i.order_id, i.user_id, o.status AS order_status FROM t_order_item_0 i LEFT JOIN t_order_0 o ON i.order_id = o.order_id WHERE item_id = ?
     */

}
