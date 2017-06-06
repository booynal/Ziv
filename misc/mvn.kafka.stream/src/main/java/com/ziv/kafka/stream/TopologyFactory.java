package com.ziv.kafka.stream;

import org.apache.kafka.streams.processor.TopologyBuilder;

/**
 * Created by ziv on 2017/6/7.
 */
public class TopologyFactory {

	public static TopologyBuilder create() {
		TopologyBuilder builder = new TopologyBuilder();

		builder.addSource("SOURCE", "src-topic")
				// add "PROCESS1" node which takes the source processor "SOURCE" as its upstream processor
				.addProcessor("PROCESS1", () -> new WordCountProcessor(), "SOURCE")

				// add "PROCESS2" node which takes "PROCESS1" as its upstream processor
				.addProcessor("PROCESS2", () -> new WordCountProcessor(), "PROCESS1")

				// add "PROCESS3" node which takes "PROCESS1" as its upstream processor
				.addProcessor("PROCESS3", () -> new WordCountProcessor(), "PROCESS1")

				// add the sink processor node "SINK1" that takes Kafka topic "sink-topic1"
				// as output and the "PROCESS1" node as its upstream processor
				.addSink("SINK1", "sink-topic1", "PROCESS1")

				// add the sink processor node "SINK2" that takes Kafka topic "sink-topic2"
				// as output and the "PROCESS2" node as its upstream processor
				.addSink("SINK2", "sink-topic2", "PROCESS2")

				// add the sink processor node "SINK3" that takes Kafka topic "sink-topic3"
				// as output and the "PROCESS3" node as its upstream processor
				.addSink("SINK3", "sink-topic3", "PROCESS3");

		return builder;
	}
}
