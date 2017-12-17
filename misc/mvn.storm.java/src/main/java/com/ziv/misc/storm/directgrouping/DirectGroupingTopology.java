package com.ziv.misc.storm.directgrouping;

import java.util.Arrays;
import java.util.List;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * 直接分组拓扑图
 * 
 * @author ziv
 * @date 2017年12月17日 下午10:30:14
 */
public class DirectGroupingTopology {

	public static void main(String[] args) {
		// send tuple
		List<String> tuples = Arrays.asList("the cow jumped over the moon", "the man went to the store and bought some candy", "four score and seven years ago", "how many apples can you eat");

		// stream name
		String spoutStreamId = "topology.flow.cycle.spout.stream";
		String splitStreamId = "topology.flow.split.bolt.stream";

		// spout
		FixedCycleSpout cycleSpout = new FixedCycleSpout(spoutStreamId, "sentence", true, tuples);

		// bolt
		SentenceSplitBolt splitBolt = new SentenceSplitBolt(splitStreamId);
		WordSumBolt sumBolt = new WordSumBolt();

		TopologyBuilder topologyBuilder = new TopologyBuilder();
		topologyBuilder.setSpout("sentence.cycle.spout", cycleSpout, 1);
		topologyBuilder.setBolt("sentence.split.bolt", splitBolt, 2).directGrouping("sentence.cycle.spout", spoutStreamId);
		topologyBuilder.setBolt("word.sum.bolt", sumBolt, 3).fieldsGrouping("sentence.split.bolt", splitStreamId, new Fields("word"));

		Config config = new Config();
		config.put(Const.SEPARATOR, " ");

		LocalCluster localCluster = new LocalCluster();
		localCluster.submitTopology("directGroupingTopology", config, topologyBuilder.createTopology());

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		localCluster.shutdown();
	}

}
