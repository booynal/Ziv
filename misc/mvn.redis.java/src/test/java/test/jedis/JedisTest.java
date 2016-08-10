package test.jedis;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import redis.clients.jedis.Jedis;

/**
 * 测试步骤：<br/>
 * 1. 在本地启动Redis的server端：src/redis-server<br/>
 * 2. 运行本单元测试
 *
 * @author ziv
 *
 */
public class JedisTest {

	private static Jedis jedis;

	@BeforeClass
	public static void setUp() {
		jedis = new Jedis("localhost");
	}

	@AfterClass
	public static void tearDown() {
		jedis.close();
	}

	@Test
	public void test() {
		String key = "foo";
		jedis.set(key, "bar");

		Boolean exists = jedis.exists(key);
		Assert.assertTrue(exists);

		String value = jedis.get("foo");
		System.out.println(value);
		Assert.assertEquals("bar", value);

		Long dbSize = jedis.dbSize();
		System.out.println("db.size = " + dbSize);
		Assert.assertTrue("db.size 应该>1", dbSize >= 1);

		Long del = jedis.del(key);
		System.out.println("成功删除记录: " + del);
		Assert.assertTrue("删除记录数应该为1", del == 1);
	}

	/**
	 * 同时设置或获取多个值<br/>
	 * m: multiple
	 */
	@Test
	public void test_mset() {
		String mset = jedis.mset("a", "b", "c", "d");
		System.out.println("mset: " + mset);

		List<String> mget = jedis.mget("a", "c");
		System.out.println("获取a和c的值: " + mget);
		Assert.assertTrue(mget.size() >= 2);
		Assert.assertEquals("b", mget.get(0));
		Assert.assertEquals("d", mget.get(1));

		Long del = jedis.del("a", "c");
		System.out.println("成功删除记录: " + del);
		Assert.assertTrue("删除记录数应该为2", del == 2);
	}

	/**
	 * hset就是按照hash设置值
	 */
	@Test
	public void test_hset() {
		jedis.del("a");
		Long hset = jedis.hset("a", "b", "c");
		System.out.println(hset);
		Assert.assertEquals(1, hset.intValue());

		String hget = jedis.hget("a", "b");
		System.out.println(hget);
		Assert.assertEquals("c", hget);
	}

}
