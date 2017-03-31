package test.jedis;

import org.junit.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import test.junit.base.BaseTest;

import java.util.List;

/**
 * Created by ziv on 2017/3/31.
 */
public class JedisExpireTest extends BaseTest {

	private static final String KEY1 = "expire1";
	private static final String KEY2 = "expire2";
	private static Jedis jedis;

	@BeforeClass
	public static void setUp() {
		jedis = new Jedis("localhost");
	}

	@AfterClass
	public static void tearDown() {
		jedis.close();
	}

	@Before
	public void cleanBeforeTest() {
		System.out.println("清理: " + jedis.del(KEY1, KEY2));
	}

	@Test
	public void test_second() throws InterruptedException {
		System.out.println(jedis.set(KEY1, "v1"));
		System.out.println(jedis.expire(KEY1, 1));

		System.out.println(jedis.setex(KEY2, 2, "v2"));

		Thread.sleep(500);

		String s = jedis.get(KEY1);
		System.out.println(s);
		Assert.assertNotNull(s);

		Thread.sleep(600);

		String s2 = jedis.get(KEY1);
		System.out.println(s2);
		Assert.assertNull(s2);

		String s3 = jedis.get(KEY2);
		System.out.println(s3);
		Assert.assertNotNull(s3);

		Thread.sleep(990);

		String s4 = jedis.get(KEY2);
		System.out.println(s4);
		Assert.assertNull(s4);
	}

	@Test
	public void test_mills() throws InterruptedException {
		System.out.println(jedis.set(KEY1, "v1"));
		System.out.println(jedis.pexpire(KEY1, 100L));

		System.out.println(jedis.psetex(KEY2, 180L, "v2"));

		Thread.sleep(50);

		String s1 = jedis.get(KEY1);
		System.out.println(s1);
		Assert.assertNotNull(s1);

		Thread.sleep(60);

		String s2 = jedis.get(KEY1);
		System.out.println(s2);
		Assert.assertNull(s2);

		String s3 = jedis.get(KEY2);
		System.out.println(s3);
		Assert.assertNotNull(s3);

		Thread.sleep(90);

		String s4 = jedis.get(KEY2);
		System.out.println(s4);
		Assert.assertNull(s4);
	}

	/**
	 * 测试点：探索批量设定过期时间的方法
	 */
	@Test
	public void test_multi_expire() throws InterruptedException {
		Jedis jedis2 = JedisExpireTest.jedis;
		setUp();
		Jedis jedisTransaction = jedis;

		System.out.println(jedis2.set(KEY1, KEY1));
		System.out.println(jedis2.set(KEY2, KEY2));
		Assert.assertEquals(-1, jedis2.ttl(KEY1).intValue());
		Assert.assertEquals(-1, jedis2.ttl(KEY2).intValue());

		Transaction multi = jedisTransaction.multi(); // 事务
		int ttl = 2;
		System.out.println(multi.expire(KEY1, ttl));
		System.out.println(multi.expire(KEY2, ttl));

		// jedisTransaction.ttl(KEY1); // 同一个jedis客户端在开启事务过程中就不能再调用别的方法，ttl()
		// 提交事务前，redis中的数据没有被改变
		Assert.assertEquals(-1, jedis2.ttl(KEY1).intValue());
		Assert.assertEquals(-1, jedis2.ttl(KEY2).intValue());

		List<Object> exec = multi.exec(); // 提交事务，redis中的数据才真正该改变
		System.out.println(exec);

		Thread.sleep(100);

		int ttl1 = jedis2.ttl(KEY1).intValue();
		System.out.println(ttl1);
		Assert.assertEquals(ttl, ttl1);
		int ttl2 = jedis2.ttl(KEY2).intValue();
		System.out.println(ttl2);
		Assert.assertEquals(ttl, ttl2);

		Thread.sleep(900);
		ttl1 = jedis2.ttl(KEY1).intValue();
		System.out.println(ttl1);
		Assert.assertEquals(ttl - 1, ttl1);
		ttl2 = jedis2.ttl(KEY2).intValue();
		System.out.println(ttl2);
		Assert.assertEquals(ttl - 1, ttl2);

	}
}
