import com.czw.demo.controller.GoodController;
import com.czw.demo.redisdemo.RedisDemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 秒杀测试(模拟100个线程同时并发)
 *
 * @author caizw25178
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisDemoApplication.class)
public class GoodTest {

    @Resource
    private GoodController goodController;

    private CyclicBarrier cyclicBarrierA = new CyclicBarrier(100);
    private CyclicBarrier cyclicBarrierB = new CyclicBarrier(100);

    public GoodTest() {

    }

    @Test
    public void secKillTest() {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    cyclicBarrierA.await();
                    goodController.secKillTest(1);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "Thread-A-" + i).start();
            new Thread(() -> {
                try {
                    cyclicBarrierB.await();
                    goodController.secKillTest(2);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "Thread-B-" + i).start();
        }
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
