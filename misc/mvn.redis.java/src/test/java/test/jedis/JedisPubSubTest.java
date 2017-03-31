package test.jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * Jedis的发布订阅测试<br/>
 * 注意：订阅线程与发布线程他们不可以共用一个jedis客户端
 *
 * @author ziv
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JedisPubSubTest {

	private static final String KEY_1 = "foo";
	private static final String KEY_2 = "bar";
	private Jedis jedis;

	@Before
	public void setUp() {
		jedis = new Jedis("localhost");
	}

	@After
	public void tearDown() {
		jedis.close();
	}

	@Test
	public void test_1_subscribe() {
		new Thread(() -> {
			/* 这里缓存jedis客户端，以免被一下轮setUp修改了 */
			Jedis jedis = this.jedis;
			JedisPubSub jedisPubSub = new JedisPubSub() {

				@Override
				public void onMessage(String channel, String message) {
					System.out.println(String.format("订阅器1：channel: '%s', message: '%s'", channel, message));
				}
			};
			System.out.println("subscribe begin");
			// 阻塞方法
			jedis.subscribe(jedisPubSub, KEY_1, KEY_2);
		}).start();
	}

	/**
	 * 订阅器2：可以同时有多个订阅器，并且订阅同样的通道
	 */
	@Test
	public void test_1_subscribe2() {
		new Thread(() -> {
			Jedis jedis = this.jedis;
			JedisPubSub jedisPubSub = new JedisPubSub() {

				@Override
				public void onMessage(String channel, String message) {
					System.out.println(String.format("订阅器2：channel: '%s', message: '%s'", channel, message));
				}
			};
			System.out.println("subscribe begin");
			// 阻塞方法
			jedis.subscribe(jedisPubSub, KEY_1, KEY_2);
		}).start();
	}

	@Test
	public void test_2_publish() throws InterruptedException {
		// 这里停顿一下，以便订阅器准备好，经过测试，如果不停顿，订阅器2收不到第一个发布的消息，但是后面发布的消息能够正常收到
		Thread.sleep(100);
		new Thread(() -> {
			Jedis jedis = this.jedis;
			for (int i = 0; i < 5; i++) {
				jedis.publish(i % 2 == 0 ? KEY_1 : KEY_2, "message" + i);
			}
		}).start();
		// 这里停顿一秒，以便订阅线程处理收到的数据
		Thread.sleep(1 * 1000);
		System.out.println("system end");
	}

}
