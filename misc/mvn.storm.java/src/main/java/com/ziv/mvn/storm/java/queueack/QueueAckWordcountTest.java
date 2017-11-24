package com.ziv.mvn.storm.java.queueack;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

/**
 * Created by ziv on 2017/8/21.
 */
public class QueueAckWordcountTest {

	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, AuthorizationException {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("spout-1", new MyWordSpout(), 1);
		builder.setBolt("bolt-1", new Bolt1(), 1).shuffleGrouping("spout-1");
		builder.setBolt("bolt-2", new Bolt2()).shuffleGrouping("bolt-1");

		Config config = new Config();
		config.setNumWorkers(3);
		config.setDebug(false);

		if (args != null && args.length > 0) {
			Config.setNumAckers(config, 3);
			config.setNumWorkers(3);
			System.out.println("准备提交topo名称 ： " + args[0]);
			StormSubmitter.submitTopology(args[0], config, builder.createTopology());
		} else {
			Config.setNumAckers(config, 1);
			LocalCluster cluster = new LocalCluster();
			String name = "QueueAckWordcountTest-topology";
			cluster.submitTopology(name, config, builder.createTopology());
			Utils.sleep(10000);
			cluster.killTopology(name);
			cluster.shutdown();
		}
	}
}
