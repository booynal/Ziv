package mvn.kafka.java.test;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import org.junit.BeforeClass;

import java.util.Properties;

/**
 * Created by ziv on 2017/4/21.
 */
public class KafkaConsumerFactory {

	public static ConsumerConnector createConsumerConnector() {
		return Consumer.createJavaConsumerConnector(new ConsumerConfig(generateProps()));
	}


	private static Properties generateProps() {
		Properties props = new Properties();
		// 必选配置
		props.put("zookeeper.connect", "localhost:2181");
		props.put("group.id", "test-group");

		// 可选配置
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("zookeeper.session.timeout.ms", "4000");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.offset.reset", "smallest");
		props.put("rebalance.max.retries", "5");
		props.put("rebalance.backoff.ms", "5000");

		return props;
	}
}
