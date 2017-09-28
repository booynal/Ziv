package mvn.storm.java.wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;
import org.junit.Test;

/**
 * Created by ziv on 2017/8/21.
 */
public class WordcountTest {

	@Test
	public void test() {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("spout-1", new MyWordSpout(), 1);
		builder.setBolt("bolt-1", new Bolt1(), 1).shuffleGrouping("spout-1");
		builder.setBolt("bolt-2", new Bolt2()).shuffleGrouping("bolt-1");

		Config config = new Config();
		config.setNumWorkers(3);
		config.setDebug(false);

		LocalCluster localCluster = new LocalCluster();
		localCluster.submitTopology("mytopology", config, builder.createTopology());
		Utils.sleep(10000);
		localCluster.killTopology("mytopology");
		localCluster.shutdown();
	}
}
