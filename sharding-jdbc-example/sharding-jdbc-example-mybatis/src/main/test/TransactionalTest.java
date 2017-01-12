import com.dangdang.ddframe.rdb.sharding.example.jdbc.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: xiezq
 * Date: 2017/1/11
 * Depiction:强一致性事务回滚
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/mybatisContext.xml"})
public class TransactionalTest {
    @Autowired
    private OrderService orderService;

    @Test
    public void fooServiceWithFailure(){
        try {
            orderService.fooServiceWithFailure();
        } catch (final IllegalArgumentException e) {
            System.out.println("roll back");
        }
        orderService.selectAll();
    }


}
