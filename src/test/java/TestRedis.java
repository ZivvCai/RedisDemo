import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * 文件描述
 *
 * @author caizw25178
 */
public class TestRedis {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestRedis.class);

    @Test
    public void test() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        try {
            jedis.set("redisDemo", "redisDemo");
        } catch (Exception e) {
            LOGGER.info("Error:{}", e.getMessage());
        } finally {
            jedis.close();
        }
    }
}
