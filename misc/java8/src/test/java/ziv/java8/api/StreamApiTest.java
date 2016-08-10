package ziv.java8.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import test.junit.base.BaseTest;

/**
 * java.util.Stream 表示能应用在一组元素上一次执行的操作序列。<br/>
 * Stream 操作分为中间操作或者最终操作两种，最终操作返回一特定类型的计算结果，而中间操作返回Stream本身，这样你就可以将多个操作依次串起来。<br/>
 * Stream 的创建需要指定一个数据源，比如 java.util.Collection的子类，List或者Set， Map不支持。<br/>
 * Stream 的操作可以串行执行或者并行执行。
 *
 * @author Booynal
 *
 */
public class StreamApiTest extends BaseTest {

	private List<String> stringCollection;

	@Before
	public void setUp() {
		stringCollection = new ArrayList<>();
		stringCollection.add("ddd2");
		stringCollection.add("aaa2");
		stringCollection.add("bbb1");
		stringCollection.add("aaa1");
		stringCollection.add("bbb3");
		stringCollection.add("ccc");
		stringCollection.add("bbb2");
		stringCollection.add("ddd1");
	}

	@Test
	public void testFilter() {
		System.out.println("filter过滤:");
		stringCollection.stream().filter((s) -> s.startsWith("a")).forEach(System.out::println);
		Stream<String> filter = stringCollection.stream().filter((s) -> s.startsWith("a"));
		Assert.assertEquals(2, filter.count());
	}

	@Test
	public void testSort() {
		System.out.println("sort排序:"); // sorted方法返回一个新的排序之后的Stream(而不会修改原来的stream)
		stringCollection.stream().sorted().filter((s) -> s.startsWith("a")).forEach(System.out::println);
		Iterator<String> iterator = stringCollection.stream().sorted().filter((s) -> s.startsWith("a")).iterator();
		Assert.assertEquals("aaa1", iterator.next());
		Assert.assertEquals("aaa2", iterator.next());
	}

	@Test
	public void testMap() {
		System.out.println("map映射:"); // 通过map来将对象转换成其他类型，map返回的Stream类型是根据你map传递进去的函数的返回值决定的
		stringCollection.stream().map(String::toUpperCase).sorted((a, b) -> a.compareTo(b)).forEach(System.out::println);
		Stream<String> mapped = stringCollection.stream().map(String::toUpperCase).sorted((a, b) -> a.compareTo(b));
		Iterator<String> iterator = mapped.iterator();
		Assert.assertEquals("AAA1", iterator.next());
		Assert.assertEquals("AAA2", iterator.next());
	}

	@Test
	public void testMatch() {
		System.out.println("match匹配:"); // 所有的匹配操作都是最终操作，并返回一个boolean类型的值
		boolean anyStartsWithA = stringCollection.stream().anyMatch((s) -> s.startsWith("a"));
		System.out.println("是否任意一项startsWith字母a: " + anyStartsWithA);
		Assert.assertTrue(anyStartsWithA);
		boolean allStartsWithA = stringCollection.stream().allMatch((s) -> s.startsWith("a"));
		System.out.println("是否所有项都startsWith字母a: " + allStartsWithA);
		Assert.assertFalse(allStartsWithA);
		boolean noneStartsWithA = stringCollection.stream().noneMatch((s) -> s.startsWith("a"));
		System.out.println("是否没有一项startsWith字母a: " + noneStartsWithA);
		Assert.assertFalse(noneStartsWithA);
	}

	@Test
	public void testCount() {
		System.out.println("count计数:"); // 计数是一个最终操作，返回Stream中元素的个数，返回值类型是long。
		long count = stringCollection.stream().count();
		System.out.println("stringCollection的元素个数: " + count);
		Assert.assertEquals(8, count);
	}

	@Test
	public void testReduce() {
		System.out.println("Reduce归纳"); // 这是一个最终操作，允许通过指定的函数来将stream中的多个元素合并为一个元素
		Optional<String> reduced = stringCollection.stream().sorted().reduce((s1, s2) -> s1 + "#" + s2);
		reduced.ifPresent(System.out::println);
		reduced.ifPresent((actual) -> Assert.assertEquals("aaa1#aaa2#bbb1#bbb2#bbb3#ccc#ddd1#ddd2", actual));
	}

	@Test
	public void testParallelStream() {
		System.out.println("并行Stream"); // Stream有串行和并行两种，串行Stream上的操作是在一个线程中依次完成，而并行Stream则是在多个线程上同时执行
		{
			// 1. 创建一个不重复的大表
			int max = 100000;
			List<String> values = new ArrayList<String>();
			for (int i = 0; i < max; i++) {
				values.add(UUID.randomUUID().toString());
			}
			// 2. 串行：
			long millisBySeq = sequentialSort(values);
			// 3. 并行
			long millisByParallel = parallelSort(values);
			Assert.assertTrue(millisBySeq > millisByParallel);
		}
	}

	private long sequentialSort(List<String> values) {
		long millisBySeq;
		long start = System.nanoTime();
		long countOfValues = values.stream().sorted().count();
		long end = System.nanoTime();
		millisBySeq = TimeUnit.NANOSECONDS.toMillis(end - start);
		System.out.println(String.format("串行排序%d个元素took: %d ms", countOfValues, millisBySeq));
		return millisBySeq;
	}

	private long parallelSort(List<String> values) {
		long millisByParallel;
		long start = System.nanoTime();
		long countOfValues = values.parallelStream().sorted().count();
		long end = System.nanoTime();
		millisByParallel = TimeUnit.NANOSECONDS.toMillis(end - start);
		System.out.println(String.format("并行排序%d个元素took: %d ms", countOfValues, millisByParallel));
		return millisByParallel;
	}

}
