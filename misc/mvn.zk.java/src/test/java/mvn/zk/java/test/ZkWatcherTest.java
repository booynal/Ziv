package mvn.zk.java.test;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.BeforeClass;
import test.junit.base.BaseTest;

import java.io.IOException;
import java.util.List;

/**
 * Created by ziv on 2017/3/11.
 */
public class ZkWatcherTest extends BaseTest {

	private static ZooKeeper zooKeeper;

	@BeforeClass
	public static void setup() throws IOException {
		zooKeeper = new ZooKeeper("localhost:2181", 3000, new TestWatcher());
	}

	/**
	 * watch事件分为两种类型：data watches 和 child watches<br/>
	 * getData()和exists()可以注册data watches，getChildren()可以注册child watches<br/>
	 * setData()会触发data watches事件, create() 和 delete()会在当前znode上触发data watches，并在其parent znode上触发child watches事件
	 **/
	@org.junit.Test
	public void test() throws IOException, KeeperException, InterruptedException {
		String path = "/consumers";
		Stat stat = new Stat();
		// getData()可以获取该节点的值，并且注册data watch类型的事件
		byte[] data = zooKeeper.getData(path, true, stat); // 如果watch参数为false，则可以取消已经注册的watch监听器
		System.out.println("stat: " + stat);
		if (null != data) {
			System.out.println("data: " + new String(data));
		}

		// getChildren()可以获取子节点，并且注册data watch和child watch类型的事件
		List<String> children = zooKeeper.getChildren("/", true, new Stat());
		//System.out.println(children);

		// setData()可以设置该节点的值，同时会触发data watch类型的事件
		Stat stat1 = zooKeeper.setData(path, "newData".getBytes(), stat.getVersion());
		zooKeeper.setData(path, "newData2".getBytes(), -1); // 多次设置该值，看console是否有多次的watch事件被触发
		zooKeeper.setData(path, "newData3".getBytes(), -1);
		//System.out.println("setData-stat: " + stat1);

		zooKeeper.setData("/", "root".getBytes(), -1);
		Thread.sleep(10000);
		// 另外，成功的create()和delete()都会分别同时触发data watch 和 child watch两种类型的事件
	}

	private static final class TestWatcher implements Watcher {
		@Override
		public void process(WatchedEvent event) {
			System.out.println("Watcher.process() " + event);
			String path = event.getPath();
			Event.KeeperState state = event.getState();
			Event.EventType type = event.getType();

			if (Event.KeeperState.SyncConnected.equals(state) && null != type) {
				switch (type) {

					case None:
						break;
					case NodeCreated:
						break;
					case NodeDeleted:
						break;
					case NodeDataChanged:
						Stat stat = new Stat();
						try {
							byte[] data = zooKeeper.getData(path, true, stat); // 如果此处的第二个参数为false，则就取消来监听path的变更了
							System.out.println(String.format("data: '%s', stat: '%s'", null == data ? null : new String(data), stat));
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case NodeChildrenChanged:
						break;
				}
			}
		}
	}
}
