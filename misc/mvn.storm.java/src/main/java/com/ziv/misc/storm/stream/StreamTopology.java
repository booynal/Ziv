package com.ziv.misc.storm.stream;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

/**
 * 创建多个流，根据元组的某些属性的不同，分别发射到不同的Bolt上<br/>
 * 只有指定streamId的Bolt会收到元组
 *
 * @author ziv
 * @date 2017年12月17日 下午10:35:20
 */
public class StreamTopology {

	public static void main(String[] args) {
		String spoutId = "color.spout";
		String boltDistributeId = "distribute.bolt";
		String boltRedId = "red.bolt";
		String boltGreenId = "green.bolt";
		String boltYellowId = "yellow.bolt";

		TopologyBuilder topologyBuilder = new TopologyBuilder();
		topologyBuilder.setSpout(spoutId, new ColorGeneratorSpout(), 1);
		topologyBuilder.setBolt(boltDistributeId, new ColorDistributeBolt(), 2).shuffleGrouping(spoutId);
		topologyBuilder.setBolt(boltRedId, new RedBolt(), 2).shuffleGrouping(boltDistributeId, StreamConsts.redStreamId);
		topologyBuilder.setBolt(boltGreenId, new GreenBolt(), 2).shuffleGrouping(boltDistributeId, StreamConsts.greenStreamId);
		topologyBuilder.setBolt(boltYellowId, new YellowBolt(), 2).shuffleGrouping(boltDistributeId, StreamConsts.yellowStreamId);

		Config config = new Config();
		LocalCluster localCluster = new LocalCluster();
		localCluster.submitTopology("streamTopology", config, topologyBuilder.createTopology());

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		localCluster.shutdown();
	}

}
