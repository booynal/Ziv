package mvn.kafka.java.test;

import kafka.admin.TopicCommand;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.I0Itec.zkclient.ZkClient;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by ziv on 2017/4/21.
 */
public class KafkaTopicTest {

	private static final String TOPIC = "test";
	private static final String TOPIC2 = "test2";
	private static final String TOPIC3 = "test3";
	private static ConsumerConnector consumer;

	@BeforeClass
	public static void setup() {
		consumer = KafkaConsumerFactory.createConsumerConnector();
		System.out.println(consumer);
	}

	private static void print(MessageAndMetadata<String, String> messageObject) {
		String thread = Thread.currentThread().getName();
		String topic = messageObject.topic();
		int partition = messageObject.partition();
		long offset = messageObject.offset();
		String key = messageObject.key();
		String message = messageObject.message();
		System.out.println(String.format("%s - topic: '%s', partition: '%s', offset: '%s', key: '%s', message: '%s'", thread, topic, partition, offset, key, message));
	}

	/**
	 * 测试点：连接一个不存在的topic<br/>
	 * 测试结果：<br/>
	 * 连接一个不存在的topic时候，不会报错，并且会在zk上监听该topic，直到该topic在zk中的节点被创建出来<br/>
	 * zk就会发出通知，consumer会通过做Rebalancing的方式连接到新创建的topic所在的broker上<br/>
	 * 然后就是正常的消费<br/>
	 **/
	@Test
	public void test_connect_not_exists_topic() {
		System.out.println("测试点：连接一个不存在的topic");
		Map<String, Integer> topicCountMap = new HashMap<>();
		topicCountMap.put(TOPIC3, 1);
		Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
		System.out.println("创建Stream成功");
		KafkaStream<byte[], byte[]> kafkaStream = messageStreams.get(TOPIC3).get(0);
		ConsumerIterator<byte[], byte[]> iterator = kafkaStream.iterator();
		System.out.println("准备迭代");
		boolean hasNext = iterator.hasNext();
		System.out.println(hasNext);
		if (hasNext) {
			MessageAndMetadata<byte[], byte[]> next = iterator.next();
			System.out.println(String.format("offset: '%s', message: '%s'", next.offset(), new String(next.message())));
		}
		System.out.println("测试结束");
	}

	/**
	 * 测试点：消费具有多个分区的topic<br/>
	 * 创建topic2：<br/>
	 * <pre>
	 * $ bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic test2
	 * </pre>
	 * 有几个分区，就可以创建几个KafkaStream，多创建的Stream无效
	 */
	@Test
	public void test_consume_multi_partition_topic() throws InterruptedException {
		System.out.println("测试点：消费具有多个分区的topic");
		Map<String, Integer> topicCountMap = new HashMap<>();
		final int count = 2;
		topicCountMap.put(TOPIC2, count); // topic2是具有两个分区的topic
		Map<String, List<KafkaStream<String, String>>> messageStreams = consumer.createMessageStreams(topicCountMap, getStringDecoder(), getStringDecoder());
		System.out.println("创建Stream成功");
		CountDownLatch countDownLatch = new CountDownLatch(count);
		ExecutorService executorService = Executors.newFixedThreadPool(count);
		System.out.println("准备迭代");
		for (int i = 0; i < count; i++) {
			KafkaStream<String, String> stream = messageStreams.get(TOPIC2).get(i);
			System.out.println("Stream: " + stream);
			executorService.submit(new ConsumerRunnable(stream, countDownLatch));
		}
		countDownLatch.await(10, TimeUnit.SECONDS);
		System.out.println("测试结束");
	}

	@Test
	public void test_topic_create() {
		String[] args = new String[]{"--topic", TOPIC, "--partitions", "1", "--replication-factor", "1"};
		ZkClient zkClient = new ZkClient("localhost:2181", 3000, 3000);
		TopicCommand.createTopic(zkClient, new TopicCommand.TopicCommandOptions(args));
		zkClient.close();
	}
	@Test
	public void test_topic_delete() {
		String[] args = new String[]{"--topic", TOPIC};
		ZkClient zkClient = new ZkClient("localhost:2181", 3000, 3000);
		TopicCommand.deleteTopic(zkClient, new TopicCommand.TopicCommandOptions(args));
		zkClient.close();
	}

	/**
	 * 测试点：同时连接多个topic，并消费数据<br/>
	 * 测试方法：<br/>
	 * 先启动本类中测试方法，再启动producer(见脚本:bin/6producer.sh)，<br/>
	 * 然后再producer.sh的标准输入中输入消息， 将会在本程序中取到消息<br/>
	 * <p>结论：由于消费者的阻塞性，所以每个topic都需要用单独的线程来消费</p>
	 */
	@Test
	public void test_consume_multi_topic() throws InterruptedException {
		System.out.println("开始创建消息流");
		Map<String, Integer> topicCountMap = new HashMap<>();
		topicCountMap.put(TOPIC, new Integer(1));
		topicCountMap.put(TOPIC2, new Integer(1));
		Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap, getStringDecoder(), getStringDecoder());
		KafkaStream<String, String> stream = consumerMap.get(TOPIC).get(0);
		KafkaStream<String, String> stream2 = consumerMap.get(TOPIC2).get(0);
		System.out.println(String.format("创建完成, Topic: '%s'='%s'", TOPIC, stream));
		System.out.println(String.format("创建完成, Topic2: '%s'='%s'", TOPIC2, stream2));

		final CountDownLatch countDownLatch = new CountDownLatch(2);
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		executorService.submit(new ConsumerRunnable(stream, countDownLatch));
		executorService.submit(new ConsumerRunnable(stream2, countDownLatch));
		countDownLatch.await();
	}

	private StringDecoder getStringDecoder() {
		return new StringDecoder(new VerifiableProperties());
	}

	private static class ConsumerRunnable implements Runnable {

		private KafkaStream<String, String> stream;
		private CountDownLatch countDownLatch;

		public ConsumerRunnable(KafkaStream<String, String> stream, CountDownLatch countDownLatch) {
			this.stream = stream;
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			try {
				System.out.println(Thread.currentThread().getName() + " 准备获取消息:(阻塞), stream: " + stream);
				ConsumerIterator<String, String> it = stream.iterator();
				while (it.hasNext()) { // hasNext()方法是一个阻塞性的方法，如果没有下一个元素了就等待
					print(it.next());
				}
				System.out.println(String.format("%s-消费结束", Thread.currentThread().getName()));
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				countDownLatch.countDown();
			}
		}
	}

}
