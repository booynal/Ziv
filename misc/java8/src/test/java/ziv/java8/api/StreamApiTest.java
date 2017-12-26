package ziv.java8.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

	private List<String> stringList;

	@Before
	public void setUp() {
		stringList = new ArrayList<>();
		stringList.add("ddd2");
		stringList.add("aaa2");
		stringList.add("bbb1");
		stringList.add("aaa1");
		stringList.add("bbb3");
		stringList.add("ccc");
		stringList.add("bbb2");
		stringList.add("ddd1");
	}

	@Test
	public void testCreate() {
		System.out.println("1. Stream.builder()");
		Stream<Object> stream = Stream.builder().add("a").add("b").add("c").build();
		stream.forEach(System.out::println);

		System.out.println("2. Stream.of()");
		Stream<String> stream2 = Stream.of("a");
		stream2.forEach(System.out::println);
		stream2 = Stream.of("a", "b", "c");
		stream2.forEach(System.out::println);

		System.out.println("3. Stream.generate()");
		Stream<Double> stream3 = Stream.generate(() -> Math.random());
		stream3.limit(3).forEach(System.out::println);
		System.out.println(Stream.generate(Math::random).limit(3).count());

		System.out.println("4. Stream.iterate()");
		Stream<Integer> stream4 = Stream.iterate(100, i -> i + 1);
		stream4.limit(3).forEach(System.out::println);

		System.out.println("5. Collection.stream()");
		Stream<String> stream5 = Arrays.asList("a", "b", "c").stream();
		stream5.forEach(System.out::println);

		System.out.println("6. Collection.parallelStream()");
		Stream<String> stream6 = Arrays.asList("a", "b", "c").parallelStream();
		stream6.forEach(System.out::println);
	}

	@Test
	public void testTransform() {
		// 综合利用stream的各种api，可以灵活的对集合中的元素做各种转换操作
		List<Integer> list = Arrays.asList(1, 1, 5, 2, 7, 3, null, 9, null, 4, 6);
		int sum = list.stream().filter(r -> null != r).distinct().mapToInt(r -> r * 10).skip(2).limit(3).sorted().peek(System.out::println).sum();
		System.out.println("sum: " + sum);
	}

	@Test
	public void testFilter() {
		System.out.println("filter过滤:");
		stringList.stream().filter((s) -> s.startsWith("a")).forEach(System.out::println);
		Stream<String> filter = stringList.stream().filter((s) -> s.startsWith("a"));
		Assert.assertEquals(2, filter.count());
	}

	@Test
	public void testSort() {
		System.out.println("sort排序:"); // sorted方法返回一个新的排序之后的Stream(而不会修改原来的stream)
		stringList.stream().sorted().filter((s) -> s.startsWith("a")).forEach(System.out::println);
		Iterator<String> iterator = stringList.stream().sorted().filter((s) -> s.startsWith("a")).iterator();
		Assert.assertEquals("aaa1", iterator.next());
		Assert.assertEquals("aaa2", iterator.next());
	}

	@Test
	public void testMap() {
		System.out.println("map映射:"); // 通过map来将对象转换成其他类型，map返回的Stream类型是根据你map传递进去的函数的返回值决定的
		stringList.stream().map(String::toUpperCase).sorted((a, b) -> a.compareTo(b)).forEach(System.out::println);
		Stream<String> mapped = stringList.stream().map(String::toUpperCase).sorted((a, b) -> a.compareTo(b));
		Iterator<String> iterator = mapped.iterator();
		Assert.assertEquals("AAA1", iterator.next());
		Assert.assertEquals("AAA2", iterator.next());
	}

	@Test
	public void test_flatMap() {
		System.out.println("flatMap扁平映射:"); // 与map不同，flatMap是每个元素转换后得到的是Stream对象，并将Stream中的元素合并到父集合中

		List<List<Integer>> listList = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4), Arrays.asList(5, 6));
		System.out.println("1. flatMap得到所有的子集合元素");
		listList.stream().flatMap(t -> t.stream()).forEach(System.out::println);
		System.out.println("2. flatMapToInt将子集合中能够被2整除的元素合并到父集合中");
		Function<? super List<Integer>, ? extends IntStream> mapper = new Function<List<Integer>, IntStream>() {

			@Override
			public IntStream apply(List<Integer> t) {
				return t.stream().mapToInt(e -> e).filter(f -> f % 2 == 0);
			}
		};
		listList.stream().flatMapToInt(mapper).forEach(System.out::println);
	}

	@Test
	public void testMatch() {
		System.out.println("match匹配:"); // 所有的匹配操作都是最终操作，并返回一个boolean类型的值
		boolean anyStartsWithA = stringList.stream().anyMatch((s) -> s.startsWith("a"));
		System.out.println("是否任意一项startsWith字母a: " + anyStartsWithA);
		Assert.assertTrue(anyStartsWithA);
		boolean allStartsWithA = stringList.stream().allMatch((s) -> s.startsWith("a"));
		System.out.println("是否所有项都startsWith字母a: " + allStartsWithA);
		Assert.assertFalse(allStartsWithA);
		boolean noneStartsWithA = stringList.stream().noneMatch((s) -> s.startsWith("a"));
		System.out.println("是否没有一项startsWith字母a: " + noneStartsWithA);
		Assert.assertFalse(noneStartsWithA);
	}

	@Test
	public void testCount() {
		System.out.println("count计数:"); // 计数是一个最终操作，返回Stream中元素的个数，返回值类型是long。
		long count = stringList.stream().count();
		System.out.println("stringCollection的元素个数: " + count);
		Assert.assertEquals(8, count);
	}

	@Test
	public void testReduce() {
		System.out.println("Reduce归纳"); // 这是一个最终操作，允许通过指定的函数来将stream中的多个元素合并为一个元素

		List<String> list = Arrays.asList("a1", "a2", "b1", "b2", "c");
		// 将所有元素用#连接成一个字符串
		// 方法签名: Optional<T> reduce(BinaryOperator<T> accumulator);
		Optional<String> reduced = list.stream().reduce((s1, s2) -> s1 + "#" + s2);
		reduced.ifPresent(System.out::println);
		reduced.ifPresent((actual) -> Assert.assertEquals("a1#a2#b1#b2#c", actual));

		// 在初始值上用#做连接
		// 方法签名: T reduce(T identity, BinaryOperator<T> accumulator);
		String reducedString = list.stream().reduce("init", (subResult, next) -> subResult + "#" + next);
		System.out.println(reducedString);
		Assert.assertEquals("init#a1#a2#b1#b2#c", reducedString);

		// 计算所有元素的字符串长度总和
		// 方法签名: <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);
		// 其中identity为初始值，accumulator为累加器，combiner为并行处理时候多个线程结果的合并
		Integer totalLength = list.stream().reduce(0, (subLength, next) -> subLength += next.length(), (r1, r2) -> r1 + r2);
		System.out.println(totalLength);
		Assert.assertEquals(9, totalLength.intValue());
	}

	@Test
	public void test_collect() {
		System.out.println("Collect收集元素"); // 将元素收集到外部容器

		List<String> list = Arrays.asList("a1", "a2", "b1", "b2", "c");
		// 计算所有元素的字符串长度，并收集到一个集合中
		// 方法签名: <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner);
		// 其中supplier为供应商，提供存放结果的容器；accumulator为累加器，提供计算逻辑；combiner为合并器，合并多线程结果
		LinkedList<Object> collect = list.stream().collect(LinkedList::new, (result, next) -> result.add(next.length()), (r1, r2) -> r1.addAll(r2));
		System.out.println(collect);
		Assert.assertArrayEquals(new Object[] { 2, 2, 2, 2, 1 }, collect.toArray());

		// 收集所有元素到一个集合，以下3种方式等价
		LinkedList<Object> collect1 = list.stream().collect(() -> new LinkedList<>(), (result, next) -> result.add(next), (r1, r2) -> r1.addAll(r2));
		LinkedList<Object> collect2 = list.stream().collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
		// 方法签名: <R, A> R collect(Collector<? super T, A, R> collector);
		// 说明：该方法非FunctionalInterface，不能用lambda表达式，但java提供来一个工具类(Collectors)来创建简单的collector，如下所示
		List<String> collect3 = list.stream().collect(Collectors.toList());
		System.out.println(collect1);
		System.out.println(collect2);
		System.out.println(collect3);
		Assert.assertArrayEquals(collect1.toArray(), collect2.toArray());
		Assert.assertArrayEquals(collect2.toArray(), collect3.toArray());
	}

	@Test
	public void testParallelStream() {
		System.out.println("并行Stream"); // Stream有串行和并行两种，串行Stream上的操作是在一个线程中依次完成，而并行Stream则是在多个线程上同时执行

		// 1. 创建一个不重复的大表
		int count = 100000;
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < count; i++) {
			values.add(UUID.randomUUID().toString());
		}
		System.out.println("ForkJoinPool并行度: " + ForkJoinPool.getCommonPoolParallelism());
		// 2. 串行：
		long millisBySeq = sequentialSort(values);
		System.out.println(String.format("串行排序'%d'个元素took: '%d'ms", count, millisBySeq));
		// 3. 并行
		long millisByParallel = parallelSort(values);
		System.out.println(String.format("并行排序'%d'个元素took: '%d'ms", count, millisByParallel));
		Assert.assertTrue(millisBySeq > millisByParallel);
	}

	private long sequentialSort(List<String> values) {
		long start = System.nanoTime();
		values.stream().sorted().count();
		long end = System.nanoTime();
		return TimeUnit.NANOSECONDS.toMillis(end - start);
	}

	private long parallelSort(List<String> values) {
		long start = System.nanoTime();
		values.parallelStream().sorted().count();
		long end = System.nanoTime();
		return TimeUnit.NANOSECONDS.toMillis(end - start);
	}

}
