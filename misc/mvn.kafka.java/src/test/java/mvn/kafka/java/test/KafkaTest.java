package mvn.kafka.java.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import org.junit.BeforeClass;
import org.junit.Test;

public class KafkaTest {

	private static ConsumerConnector consumer;
	private static final String TOPIC = "test";

	/**
	 * 我犯过的错：<br/>
	 * props放入配置的时候，把数字按照整型类型放入了，导致Kafka验证配置的时候报错<br/>
	 * 具体原因：<br/>
	 * java.util.Properties.getProperty(String)中获取到的值如果不是String类型， 则按照null处理。<br/>
	 * 导致结果：<br/>
	 * 如果放进去的时整型，则获取到的值是null，再调用getInt的时候，将报出: NumberFormatException
	 *
	 * <pre>
	 * Object getProperty(String){
	 *   Object oval = super.get(key);
	 *   String sval = (oval instanceof String) ? (String) oval : null;
	 *   ...
	 * }
	 * </pre>
	 */
	@BeforeClass
	public static void createConsumerConnector() {
		consumer = KafkaConsumerFactory.createConsumerConnector();
		System.out.println(consumer);
	}

	/**
	 * 测试方法：<br/>
	 * 先启动本类中测试方法，再启动producer(见脚本:bin/5producer.sh)，<br/>
	 * 然后再producer.sh的标准输入中输入消息， 将会在本程序中取到消息
	 */
	@Test
	public void test() {
		System.out.println("开始创建消息流");
		Map<String, Integer> topicCountMap = new HashMap<>();
		topicCountMap.put(TOPIC, new Integer(1));
		Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap, new StringDecoder(new VerifiableProperties()), new StringDecoder(
				new VerifiableProperties()));
		KafkaStream<String, String> stream = consumerMap.get(TOPIC).get(0);
		System.out.println(String.format("创建完成, Topic: '%s'='%s'", TOPIC, stream));

		ConsumerIterator<String, String> it = stream.iterator();
		System.out.println("准备进入取消息队列:(阻塞)");
		while (it.hasNext()) { // hasNext()方法是一个阻塞性的方法，如果没有下一个元素了就等待
			print(it.next());
		}
	}

	private void print(MessageAndMetadata<String, String> messageObject) {
		String topic = messageObject.topic();
		int partition = messageObject.partition();
		long offset = messageObject.offset();
		String key = messageObject.key();
		String message = messageObject.message();
		System.out.println(String.format("topic: '%s', partition: '%s', offset: '%s', key: '%s', message: '%s'", topic, partition, offset, key, message));
	}

}
