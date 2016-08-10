package test.jedis;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;
import test.junit.base.BaseTest;

/**
 * Redis的list操作测试<br/>
 * 测试步骤：<br/>
 * 1. 在本地启动Redis的server端：src/redis-server<br/>
 * 2. 运行本单元测试
 *
 * <pre>
 *
 * list是一个链表结构，主要功能是push、pop
 * 
 * 1. lpush		增，在list的头部(队列的左边)添加元素(压入栈)
 * 2. rpush		增，在list的尾部(队列的右边)添加元素(压入栈)
 * 3. linsert	增，在list的指定元素(注意：是元素而不是下标)的前或后添加元素
 * 4. lset		改，设置list中指定下标的元素值
 * 5. lrem		删，从list中删除count个和value等值的元素
 * 6. ltrim		删，保留list的指定范围(下标)内的数据。ltrim mylist 1 -1
 * 7. lpop		删，从list的头部删除元素，并返回这个元素
 * 8. rpop		删，从list的尾部删除元素，并返回这个元素
 * 9. rpoplpush	删增，从第一个list的尾部移除元素并添加到第二个list的头部，最后返回被移除的元素值，原子操作
 * 10. lindex	查，返回list的index位置的元素
 * 11. llen		查，返回list的长度
 * 12. lrange	查，查看list的元素，指定start和stop下标
 * 
 * 命令参数&格式：
 * 1. lpush key value [value ...]
 * 2. rpus key value [value ...]
 * 3. linsert key BEFORE|AFTER pivot value
 * 4. lset key index value
 * 5. lrem key count value
 * 6. ltrim key start stop
 * 7. lpop key
 * 8. rpop key
 * 9. rpoplpush source destination
 * 10. lindex key value
 * 11. llen key
 * 12. lrange key start stop
 *
 * </pre>
 *
 * @author ziv
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JedisListTest extends BaseTest {

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
		System.out.println("清理: " + jedis.del(key));
	}

	private String key = "mylist";

	/**
	 * 1. lpush 增，在list的头部(队列的左边)添加元素(压入栈)
	 */
	@Test
	public void test_1_lpush() {
		// 先插入a，再在a的左边插入b，最终的顺序是b、a
		Long lpush = jedis.lpush(key, "a", "b");
		System.out.println("插入: " + lpush);

		List<String> lrange = jedis.lrange(key, 0, -1); // 获取从0到最后一个元素
		System.out.println(lrange);
		Assert.assertEquals("b", lrange.get(0));
		Assert.assertEquals("a", lrange.get(1));

		Long del = jedis.del(key);
		System.out.println("删除: " + del);
	}

	/**
	 * 2. rpush 增，在list的尾部(队列的右边)添加元素(压入栈)
	 */
	@Test
	public void test_2_rpush() {
		// 先插入a，再在a的右边插入b，最终的顺序是a、b
		Long rpush = jedis.rpush(key, "a", "b");
		System.out.println("插入: " + rpush);

		List<String> lrange = jedis.lrange(key, 0, -1);
		System.out.println(lrange);
		Assert.assertEquals("a", lrange.get(0));
		Assert.assertEquals("b", lrange.get(1));

		Long del = jedis.del(key);
		System.out.println("删除: " + del);
	}

	/**
	 * 3. linsert 增，在list的指定元素(注意：是元素而不是下标)的前或后添加元素
	 */
	@Test
	public void test_3_linsert() {
		Long rpush = jedis.rpush(key, "a", "b");
		System.out.println("插入: " + rpush);

		// 之前插入
		System.out.println("linsert: " + jedis.linsert(key, LIST_POSITION.BEFORE, "b", "c"));
		// 之后插入
		System.out.println("linsert: " + jedis.linsert(key, LIST_POSITION.AFTER, "b", "d"));

		List<String> lrange = jedis.lrange(key, 0, -1);
		System.out.println(lrange);
		Assert.assertEquals("a", lrange.get(0));
		Assert.assertEquals("c", lrange.get(1));
		Assert.assertEquals("b", lrange.get(2));
		Assert.assertEquals("d", lrange.get(3));

		Long del = jedis.del(key);
		System.out.println("删除: " + del);
	}

	/**
	 * 4. lset 改，设置list中指定下标的元素值
	 */
	@Test
	public void test_4_lset() {
		Long rpush = jedis.rpush(key, "a", "b");
		System.out.println("插入: " + rpush);

		System.out.println(jedis.lset(key, 0, "c"));
		System.out.println(jedis.lset(key, 1, "d"));

		List<String> lrange = jedis.lrange(key, 0, -1);
		System.out.println(lrange);
		Assert.assertEquals("c", lrange.get(0));
		Assert.assertEquals("d", lrange.get(1));

		Long del = jedis.del(key);
		System.out.println("删除: " + del);
	}

	/**
	 * 当不存在这个key的时候，会抛出异常
	 */
	@Test
	public void test_4_lset_not_exists_key() {
		try {
			jedis.lset("not_exists_key", 0, "value");
		} catch (JedisDataException e) {
			String expected = "ERR no such key";
			Assert.assertEquals(expected, e.getMessage());
		}
	}

	@Test
	public void test_4_lset_wrong_type() {
		jedis.set("exists_key", "b");
		try {
			jedis.lset("exists_key", 0, "value");
			Assert.assertFalse("这块代码不应该被执行", true);
		} catch (JedisDataException e) {
			String expected = "WRONGTYPE Operation against a key holding the wrong kind of value";
			Assert.assertEquals(expected, e.getMessage());
		}
	}

	/**
	 * 5. lrem 删，从list中删除count个和value等值的元素
	 */
	@Test
	public void test_5_lrem() {
		Long rpush = jedis.rpush(key, "a", "b", "a");
		System.out.println("插入: " + rpush);

		System.out.println(jedis.lrem(key, 2, "a"));

		List<String> lrange = jedis.lrange(key, 0, -1);
		System.out.println(lrange);
		Assert.assertEquals("b", lrange.get(0));

		Long del = jedis.del(key);
		System.out.println("删除: " + del);
	}

	/**
	 * 6. ltrim 删，保留list的指定范围(下标)内的数据。ltrim mylist 1 -1
	 */
	@Test
	public void test_6_ltrim() {
		Long rpush = jedis.rpush(key, "a", "b", "c", "d");
		System.out.println("插入: " + rpush);

		// 仅保留下标为1和2的元素.[start, end](左闭右闭区间)
		System.out.println(jedis.ltrim(key, 1, 2));

		Long llen = jedis.llen(key);
		System.out.println("llen: " + llen);
		Assert.assertEquals(2, llen.intValue());

		List<String> lrange = jedis.lrange(key, 0, -1);
		System.out.println(lrange);
		Assert.assertEquals("b", lrange.get(0));
		Assert.assertEquals("c", lrange.get(1));

		Long del = jedis.del(key);
		System.out.println("删除: " + del);
	}

	/**
	 * 7. lpop 删，从list的头部删除元素，并返回这个元素
	 */
	@Test
	public void test_7_lpop() {
		Long rpush = jedis.rpush(key, "a", "b", "c");
		System.out.println("插入: " + rpush);

		String lpop = jedis.lpop(key);
		System.out.println("lpop: " + lpop);
		Assert.assertEquals("a", lpop);

		Long llen = jedis.llen(key);
		System.out.println("llen: " + llen);
		Assert.assertEquals(2, llen.intValue());

		List<String> lrange = jedis.lrange(key, 0, -1);
		System.out.println(lrange);
		Assert.assertEquals("b", lrange.get(0));
		Assert.assertEquals("c", lrange.get(1));

		Long del = jedis.del(key);
		System.out.println("删除: " + del);
	}

	/**
	 * 8. rpop 删，从list的尾部删除元素，并返回这个元素
	 */
	@Test
	public void test_8_rpop() {
		Long rpush = jedis.rpush(key, "a", "b", "c");
		System.out.println("插入: " + rpush);

		String rpop = jedis.rpop(key);
		System.out.println("rpop: " + rpop);
		Assert.assertEquals("c", rpop);

		Long llen = jedis.llen(key);
		System.out.println("llen: " + llen);
		Assert.assertEquals(2, llen.intValue());

		List<String> lrange = jedis.lrange(key, 0, -1);
		System.out.println(lrange);
		Assert.assertEquals("a", lrange.get(0));
		Assert.assertEquals("b", lrange.get(1));

		Long del = jedis.del(key);
		System.out.println("删除: " + del);
	}

	/**
	 * 9. rpoplpush 删增，从第一个list的尾部移除元素并添加到第二个list的头部，最后返回被移除的元素值，原子操作
	 */
	@Test
	public void test_9_rpoplpush() {
		String dstKey = "mylist2";
		System.out.println("清理: " + jedis.del(dstKey));

		Long rpush = jedis.rpush(key, "a", "b");
		System.out.println("插入: " + rpush);
		Long rpush2 = jedis.rpush(dstKey, "c", "d");
		System.out.println("插入: " + rpush2);

		String rpoplpush = jedis.rpoplpush(key, dstKey);
		System.out.println("rpoplpush: " + rpoplpush);

		// 第一个list的长度应该是1
		Long llen = jedis.llen(key);
		System.out.println("llen: " + llen);
		Assert.assertEquals(1, llen.intValue());

		// 第二个list的长度应该是3
		Long llen2 = jedis.llen(dstKey);
		System.out.println("llen2: " + llen2);
		Assert.assertEquals(3, llen2.intValue());

		List<String> lrange = jedis.lrange(key, 0, -1);
		System.out.println(lrange);
		Assert.assertEquals("a", lrange.get(0));

		List<String> lrange2 = jedis.lrange(dstKey, 0, -1);
		System.out.println(lrange2);
		Assert.assertEquals("b", lrange2.get(0));
		Assert.assertEquals("c", lrange2.get(1));
		Assert.assertEquals("d", lrange2.get(2));

		Long del = jedis.del(key, dstKey);
		System.out.println("删除: " + del);
	}

	/**
	 * 10. lindex 查，返回list的index位置的元素
	 */
	@Test
	public void test_10_lindex() {
		Long rpush = jedis.rpush(key, "a", "b");
		System.out.println("插入: " + rpush);

		String lindex = jedis.lindex(key, 0);
		System.out.println("lindex: " + lindex);
		Assert.assertEquals("a", lindex);

		String lindex2 = jedis.lindex(key, 1);
		System.out.println("lindex: " + lindex2);
		Assert.assertEquals("b", lindex2);

		Long del = jedis.del(key);
		System.out.println("删除: " + del);
	}

	/**
	 * 11. llen 查，返回list的长度
	 */
	@Test
	public void test_11_llen() {
		Long rpush = jedis.rpush(key, "a", "b");
		System.out.println("插入: " + rpush);

		Long llen = jedis.llen(key);
		System.out.println("llen: " + llen);
		Assert.assertEquals(2, llen.intValue());

		Long del = jedis.del(key);
		System.out.println("删除: " + del);
	}

	/**
	 * 12. lrange 查，查看list的元素，指定start和stop下标。[start, end](左闭右闭区间)
	 */
	@Test
	public void test_12_lrange() {
		Long rpush = jedis.rpush(key, "a", "b", "c", "d");
		System.out.println("插入: " + rpush);

		// 获取元素：从第一个到最后一个
		List<String> lrange = jedis.lrange(key, 0, -1);
		System.out.println("lrange1: " + lrange);
		Assert.assertEquals(4, lrange.size());

		// 获取元素：从第1个到第2个
		List<String> lrange2 = jedis.lrange(key, 0, 1);
		System.out.println("lrange2: " + lrange2);
		Assert.assertEquals(2, lrange2.size());
		Assert.assertEquals("a", lrange2.get(0));
		Assert.assertEquals("b", lrange2.get(1));

		// 获取元素：从第2个到第3个
		List<String> lrange3 = jedis.lrange(key, 1, 2);
		System.out.println("lrange3: " + lrange3);
		Assert.assertEquals(2, lrange3.size());
		Assert.assertEquals("b", lrange3.get(0));
		Assert.assertEquals("c", lrange3.get(1));

		// 获取元素：从第2个到最后一个
		List<String> lrange4 = jedis.lrange(key, 1, -1);
		System.out.println("lrange4: " + lrange4);
		Assert.assertEquals(3, lrange4.size());
		Assert.assertEquals("b", lrange4.get(0));
		Assert.assertEquals("c", lrange4.get(1));
		Assert.assertEquals("d", lrange4.get(2));

		System.out.println("删除: " + jedis.del(key));
	}

}
