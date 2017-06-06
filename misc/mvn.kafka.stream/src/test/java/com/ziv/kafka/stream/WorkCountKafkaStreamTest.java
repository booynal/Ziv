package com.ziv.kafka.stream;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.junit.Test;

import java.util.Properties;

/**
 * Created by ziv on 2017/6/7.
 */
public class WorkCountKafkaStreamTest {

	@Test
	public void test() throws InterruptedException {
		// Use the builders to define the actual processing topology, e.g. to specify
		// from which input topics to read, which stream operations (filter, map, etc.)
		// should be called, and so on.

		//KStreamBuilder builder = ...;  // when using the Kafka Streams DSL
		TopologyBuilder builder = TopologyFactory.create(); // when using the Processor API
		StreamsConfig config = getStreamsConfig();


		KafkaStreams streams = new KafkaStreams(builder, config);

		streams.start();
		streams.setUncaughtExceptionHandler((t, e) -> {
			System.err.println("t" + t + ", msg: " + e.getMessage());
		});

		Thread.sleep(10000);

		streams.close();
	}

	private StreamsConfig getStreamsConfig() {
		// Use the configuration to tell your application where the Kafka cluster is,
		// which serializers/deserializers to use by default, to specify security settings,
		// and so on.
		Properties settings = new Properties();
		settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-first-streams-application");
		settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

		// Customize the Kafka consumer settings
		settings.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 60000);

		// Customize a common client setting for both consumer and producer
		settings.put(CommonClientConfigs.RETRY_BACKOFF_MS_CONFIG, 100L);

		// Customize different values for consumer and producer
		settings.put("consumer." + ConsumerConfig.RECEIVE_BUFFER_CONFIG, 1024 * 1024);
		settings.put("producer." + ProducerConfig.RECEIVE_BUFFER_CONFIG, 64 * 1024);
		// Alternatively, you can use
		//streamsSettings.put(StreamsConfig.consumerPrefix(ConsumerConfig.RECEIVE_BUFFER_CONFIG), 1024 * 1024);
		//streamsSettings.put(StremasConfig.producerConfig(ProducerConfig.RECEIVE_BUFFER_CONFIG), 64 * 1024);
		return new StreamsConfig(settings);
	}
}
