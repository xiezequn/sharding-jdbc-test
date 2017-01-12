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

    @Test//左连接，查询结果不符
    public void selectLeftJoin(){
        orderItemMapper.selectLeftJoin(100001L);
    }

}
