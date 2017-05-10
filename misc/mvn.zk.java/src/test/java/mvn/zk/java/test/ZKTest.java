package mvn.zk.java.test;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import test.junit.base.BaseTest;

import java.io.IOException;
import java.util.List;

/**
 * Created by ziv on 2017/3/10.
 */
public class ZKTest extends BaseTest {

	private static final String path = "/node1";
	private static final String path2 = "/node2_tmp";
	private static final String path3 = "/node3_tmp_sequence";
	private static ZooKeeper zooKeeper;

	@BeforeClass
	public static void setup() throws IOException {
		zooKeeper = new ZooKeeper("localhost:2181", 3000, new MyWatcher());
	}

	@AfterClass
	public static void clean() throws InterruptedException {
		zooKeeper.close();
	}

	@Test
	public void test_newNode() throws KeeperException, InterruptedException {
		System.out.println("测试点：创建各种类型的znode节点，期待创建成功");
		byte[] data = "new".getBytes();

		if (zooKeeper.exists(path, true) != null) {
			// 由于node1为持久化类型的节点，所以上一次运行后该节点不会自动删除，需要手动删除一下，以保证本次可以创建该节点
			zooKeeper.delete(path, -1);
		}
		// 创建一个持久化的节点，该节点会一直保留在zk，直到被删除
		String path1 = zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("created: " + path1);

		// 创建一个临时节点：该节点的生命周期与该程序（zk客户端）的生命周期一致
		String path2 = zooKeeper.create(ZKTest.path2, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("created: " + path2);

		// 创建一个临时的带计数后缀的节点：实际创建的节点名称=期望名称+唯一的10位数序列后缀
		String path3 = zooKeeper.create(ZKTest.path3, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("created: " + path3);

		// 创建一个持久化带计数后缀的节点
		String path4 = zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
		System.out.println("created: " + path4);

		// 创建一个叶子节点（父节点不存在）时候会抛异常
		// zooKeeper.create("/non-exists/targetNode", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}

	@Test
	public void test_常规测试() throws IOException, KeeperException, InterruptedException {
		System.out.println("测试点：测试设置数据和获取数据，期待设置的数据能够被再次获取出来");

		long sessionId = zooKeeper.getSessionId();
		System.out.println("sessionId: " + sessionId);

		List<ACL> acl = zooKeeper.getACL("/", new Stat());
		System.out.println("acl: " + acl);

		Stat exists = zooKeeper.exists(path, true);
		System.out.println("exists: " + exists);

		List<String> children = zooKeeper.getChildren(path, true);
		System.out.println("children: " + children);

		String newData = "10";
		System.out.println(String.format("set '%s' = '%s'", path, newData));
		System.out.println(zooKeeper.setData(path, newData.getBytes(), -1));

		Stat stat = new Stat();
		String data = new String(zooKeeper.getData(path, true, stat));
		System.out.println(String.format("get '%s': '%s', stat: '%s'", path, data, stat));
		Assert.assertEquals(newData, data);

		newData = "20";
		System.out.println(String.format("set '%s' = '%s'", path, newData));
		System.out.println(zooKeeper.setData(path, newData.getBytes(), -1));

		data = new String(zooKeeper.getData(path, true, stat = new Stat()));
		System.out.println(String.format("get '%s': '%s', stat: '%s'", path, data, stat));
		Assert.assertEquals(newData, data);
	}

	@Test
	public void test_setNullData() throws KeeperException, InterruptedException {
		System.out.println("测试点：设置一个null值作为data的值，期待不抛出异常");
		Stat stat = zooKeeper.setData("/", null, -1);
		System.out.println("setNull: " + stat);

		byte[] data = zooKeeper.getData("/", false, null);
		System.out.println("getData: " + (null == data ? null : new String(data)));
		Assert.assertNull(data);
	}

	private static class MyWatcher implements Watcher {
		@Override
		public void process(WatchedEvent event) {
			System.out.println(getClass().getSimpleName() + ".process() " + event);
		}
	}
}
