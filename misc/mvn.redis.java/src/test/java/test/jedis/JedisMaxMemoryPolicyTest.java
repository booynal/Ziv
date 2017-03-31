package test.jedis;

import org.junit.*;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.TreeSet;

/**
 * 测试点：探索LRU（最近最少使用）和TTL（存活时间）算法在redis内存达到上限后保留我们指定的key不被移除的方法<br/>
 * 依赖条件：将redis的最大内存设置为1mb(1048576 bytes)，但是redis自身已经消耗了971kb，所以剩余41kb可以用来存储数据<br/>
 * Created by ziv on 2017/3/31.
 */
public class JedisMaxMemoryPolicyTest {

	private static final String KEY_PREFIX = "key";
	private static final String TARGET_KEY = "targetKey"; // 想要保留的key：期待这个key不会被移除
	private static final String TARGET_VALUE = "targetValue";
	private static Jedis jedis;

	@BeforeClass
	public static void setUp() {
		jedis = new Jedis("localhost");
		String select = jedis.select(9);
		System.out.println("select: " + select);
	}

	@AfterClass
	public static void tearDown() {
		jedis.close();
	}

	@Before
	public void cleanBeforeTest() {
		System.out.println("清理: " + jedis.flushDB());
	}

	/**
	 * 测试方法：创建22个key，每个key大小为100 byte，总共是1.2k，大于配置中指定的最大值，将触发redis移除部分key<br/>
	 * 测试结果：失败，key没有被保留下来
	 * 说明：将redis的maxmemory-policy配置改为allkey-lru<br/>
	 */
	@Test
	public void test_allkey_lru() throws InterruptedException {
		// 41040 byte
		final int count = 22;
		jedis.set(TARGET_KEY, TARGET_VALUE);
		for (int i = 0; i < count; i++) {
			jedis.set(KEY_PREFIX + i, new String(new byte[2000]));
			//jedis.set(targetKey, jedis.get(targetKey));
			System.out.println(jedis.append(TARGET_KEY, ""));
			Thread.sleep(10);
			System.out.println(String.format("index: %s, dbsize: %s", i, jedis.dbSize()));
		}

		Thread.sleep(100); // 睡眠，以便redis有时间清理超过内存限制的key

		Set<String> keys = jedis.keys("*");
		System.out.println(String.format("(%s)%s", keys.size(), new TreeSet<>(keys)));

		String s = jedis.get(TARGET_KEY);
		System.out.println("targetKey: " + s);
		Assert.assertEquals(TARGET_VALUE, s);
	}

	/**
	 * 测试目标：判断该方法是否可以保留目标key不被回收<br/>
	 * 测试结果：失败，大约在第25次插入之后，目标key就被删除了<br/>
	 * 说明：将redis的maxmemory-policy配置改为volatile-lru<br/>
	 */
	@Test
	public void test_volatile_lru() throws InterruptedException {
		final int count = 122;
		int seconds = 5000;
		jedis.setex(TARGET_KEY, seconds, TARGET_VALUE);
		for (int i = 0; i < count; i++) {
			jedis.setex(String.format("%s%02d", KEY_PREFIX, i), seconds, new String(new byte[2000]));
			Long expire = jedis.expire(TARGET_KEY, seconds);
			if (expire == 0) {
				throw new InterruptedException("key已被删除");
			}
			Thread.sleep(10);
			System.out.println(String.format("index: %s, dbsize: %s", i, jedis.dbSize()));
		}

		Thread.sleep(100); // 睡眠，以便redis有时间清理超过内存限制的key

		Set<String> keys = jedis.keys("*");
		System.out.println(String.format("(%s)%s", keys.size(), new TreeSet<>(keys)));

		String s = jedis.get(TARGET_KEY);
		System.out.println("targetKey: " + s);
		Assert.assertEquals(TARGET_VALUE, s);
	}

	/**
	 * 测试目标：判断该方法是否可以保留目标key不被回收<br/>
	 * 测试结果：成功，目标key被保留下来了<br/>
	 * 说明：将redis的maxmemory-policy配置改为volatile-ttl<br/>
	 * 说明：并非每次都成功，如果插入数据的频率太高，也可能被删除，或者抛出OOM异常<br/>
	 */
	@Test
	public void test_volatile_ttl() throws InterruptedException {
		final int count = 10000;
		int seconds = 5000;
		int targetKeySeconds = seconds * 2; // 如果重要的key的ttl更长的话，可以提高其存活的概率
		jedis.setex(TARGET_KEY, seconds, TARGET_VALUE);
		for (int i = 0; i < count; i++) {
			jedis.setex(KEY_PREFIX + i, seconds, new String(new byte[2000]));
			Long expire = jedis.expire(TARGET_KEY, targetKeySeconds);
			if (expire == 0) {
				String info = String.format("index: %s, dbsize: %s", i, jedis.dbSize());
				throw new InterruptedException(String.format("key已被删除. '%s'", info));
			}
			Thread.sleep(1);
		}

		Set<String> keys = jedis.keys("*");
		System.out.println(String.format("(%s)%s", keys.size(), new TreeSet<>(keys)));

		String s = jedis.get(TARGET_KEY);
		System.out.println("targetKey: " + s);
		Assert.assertEquals(TARGET_VALUE, s);
	}

	/**
	 * 测试目标：在上一个方法的基础上，减慢插入时间，同时将目标key的过期时间设置不用乘以2<br/>
	 * 测试结果：<br/>
	 * 说明：将redis的maxmemory-policy配置改为volatile-ttl<br/>
	 */
	@Test
	public void test_volatile_ttl_slow() throws InterruptedException {
		final int count = 100;
		int seconds = 3600 * 24 * 30 * 3;
		int targetKeySeconds = seconds;
		jedis.setex(TARGET_KEY, seconds, TARGET_VALUE);
		for (int i = 0; i < count; i++) {
			jedis.setex(KEY_PREFIX + i, seconds, new String(new byte[2000]));
			Long expire = jedis.expire(TARGET_KEY, targetKeySeconds);
			if (expire == 0) {
				String info = String.format("index: %s, dbsize: %s", i, jedis.dbSize());
				throw new InterruptedException(String.format("key已被删除. '%s'", info));
			}
			Thread.sleep(100); // 比上一个方法延长了100倍的间隔时间
		}

		Set<String> keys = jedis.keys("*");
		System.out.println(String.format("(%s)%s", keys.size(), new TreeSet<>(keys)));

		String s = jedis.get(TARGET_KEY);
		System.out.println("targetKey: " + s);
		Assert.assertEquals(TARGET_VALUE, s);
	}
}
