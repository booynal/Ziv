package mvn.kafka.java.test;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ziv on 2017/4/21.
 */
public class KafkaTopicTest {

	private static ConsumerConnector consumer;
	private static final String TOPIC = "test3";

	@BeforeClass
	public static void setup(){
		consumer = KafkaConsumerFactory.createConsumerConnector();
	}

	@Test
	public void test_connect_notexists_topic(){
		System.out.println("测试点：连接一个不存在的topic");
		Map<String, Integer> topicCountMap = new HashMap<>();
		topicCountMap.put(TOPIC, 1);
		Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> kafkaStream = messageStreams.get(TOPIC).get(0);
		ConsumerIterator<byte[], byte[]> iterator = kafkaStream.iterator();
		boolean hasNext = iterator.hasNext();
		if (hasNext) {
			MessageAndMetadata<byte[], byte[]> next = iterator.next();
			long offset = next.offset();
			byte[] message = next.message();
			System.out.println(String.format("offset: '%s', message: '%s'", offset, new String(message)));
		}
		System.out.println(hasNext);
		System.out.println("测试结束");
	}
}
