package test.storm.metric;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.metric.LoggingMetricsConsumer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

/**
 * WordCountMetricTest.java
 */

/**
 * @author ziv
 * @date 2017年9月27日 下午4:48:24
 */
public class WordCountMetricTest {

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("spout-1", new MyWordSpout(), 1);
		builder.setBolt("bolt-1", new Bolt1(), 1).shuffleGrouping("spout-1");

		Config config = new Config();
		config.setDebug(false);

		// 重点：设置metric消费者，这里设置之后，就不用再去storm.yaml文件添加配置了
		config.registerMetricsConsumer(LoggingMetricsConsumer.class, 2);

		if (args != null && args.length > 0) {
			Config.setNumAckers(config, 3);
			config.setNumWorkers(3);
			System.out.println("准备提交topo名称 ： " + args[0]);
			StormSubmitter.submitTopology(args[0], config, builder.createTopology());
		} else {
			Config.setNumAckers(config, 1);
			LocalCluster cluster = new LocalCluster();
			String name = "metric-topology-test";
			cluster.submitTopology(name, config, builder.createTopology());
			Utils.sleep(10000);
			cluster.killTopology(name);
			cluster.shutdown();
		}
	}
}
