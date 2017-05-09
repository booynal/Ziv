package mvn.kafka.java.test;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaMultiTopicTest {

	private static final String TOPIC = "test";
	private static final String TOPIC2 = "test2";
	private static ConsumerConnector consumer;

	@BeforeClass
	public static void createConsumerConnector() {
		consumer = KafkaConsumerFactory.createConsumerConnector();
		System.out.println(consumer);
	}

	/**
	 * 测试方法：<br/>
	 * 先启动本类中测试方法，再启动producer(见脚本:bin/6producer.sh)，<br/>
	 * 然后再producer.sh的标准输入中输入消息， 将会在本程序中取到消息
	 */
	@Test
	public void test() throws InterruptedException {
		System.out.println("开始创建消息流");
		Map<String, Integer> topicCountMap = new HashMap<>();
		topicCountMap.put(TOPIC, new Integer(1));
		topicCountMap.put(TOPIC2, new Integer(1));
		Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap, new StringDecoder(new VerifiableProperties()), new StringDecoder(
				new VerifiableProperties()));
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

	private void print(MessageAndMetadata<String, String> messageObject) {
		String topic = messageObject.topic();
		int partition = messageObject.partition();
		long offset = messageObject.offset();
		String key = messageObject.key();
		String message = messageObject.message();
		System.out.println(String.format(Thread.currentThread().getName() + "topic: '%s', partition: '%s', offset: '%s', key: '%s', message: '%s'", topic, partition, offset, key, message));
	}

	private class ConsumerRunnable implements Runnable {

		private KafkaStream<String, String> stream;
		private CountDownLatch countDownLatch;

		public ConsumerRunnable(KafkaStream<String, String> stream, CountDownLatch countDownLatch) {
			this.stream = stream;
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			try {
				ConsumerIterator<String, String> it = stream.iterator();
				System.out.println(Thread.currentThread().getName() + " 准备获取消息:(阻塞)");
				while (it.hasNext()) { // hasNext()方法是一个阻塞性的方法，如果没有下一个元素了就等待
					print(it.next());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				countDownLatch.countDown();
			}
		}
	}

}
