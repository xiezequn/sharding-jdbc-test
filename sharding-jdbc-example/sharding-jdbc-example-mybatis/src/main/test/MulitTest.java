import com.google.gson.Gson;
import com.share.multikeys.entity.Item;
import com.share.multikeys.entity.Order;
import com.share.multikeys.service.ItemService;
import com.share.multikeys.service.OrderService;
import com.share.multikeys.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Depiction:多片键测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/multiMybatisContext.xml"})
public class MulitTest {

    private Gson gson=new Gson();

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Test
    public void insert(){
        Order criteria = new Order();
        criteria.setUserId(11);
        criteria.setOrderId(105);
        criteria.setStatus("INSERT_TEST_12");
        criteria.setCreateTime(DateUtil.stringToDate("2016-03-03 17:08:38", "yyyy-MM-dd HH:mm:ss"));
        orderService.insert(criteria);
    }

    @Test
    public void selectByKey(){
        System.out.println(orderService.selectByKey(1));
    }

    @Test
    public void selectByKeyOrTime(){
        orderService.selectByKeyOrTime(1,DateUtil.stringToDate("2016-10-08 17:08:38", "yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void selectBetweenTime(){
        orderService.selectBetweenTime(DateUtil.addDays(new Date(), -30),new Date());
    }

    @Test
    public void selectRangeTime(){
        orderService.selectRangeTime(DateUtil.addDays(new Date(), -30),new Date());
    }

    @Test
    public void insertItem(){
        Item item=new Item();
        item.setItemId(15);
        item.setOrderId(1);
        item.setItemName("testName");
        itemService.insert(item);

    }

    @Test
    public void selectJoin(){
        itemService.selectJoin(1);
    }









}
