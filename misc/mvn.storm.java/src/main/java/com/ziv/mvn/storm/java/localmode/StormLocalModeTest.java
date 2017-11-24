package com.ziv.mvn.storm.java.localmode;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.testing.TestGlobalCount;
import org.apache.storm.testing.TestWordCounter;
import org.apache.storm.testing.TestWordSpout;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

/**
 * Created by ziv on 2017/8/15.
 */
public class StormLocalModeTest {

	public static void main(String[] args) {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("1", new TestWordSpout(true), 5);
		builder.setSpout("2", new TestWordSpout(true), 3);
		builder.setBolt("3", new TestWordCounter(), 3).fieldsGrouping("1", new Fields("word")).fieldsGrouping("2", new Fields("word"));
		builder.setBolt("4", new TestGlobalCount()).globalGrouping("1");

		Config conf = new Config();
		conf.put(Config.TOPOLOGY_WORKERS, 4);
		conf.put(Config.TOPOLOGY_DEBUG, true);

		LocalCluster localCluster = new LocalCluster();
		localCluster.submitTopology("mytopology", conf, builder.createTopology());
		Utils.sleep(10000);
		localCluster.shutdown();
	}
}
