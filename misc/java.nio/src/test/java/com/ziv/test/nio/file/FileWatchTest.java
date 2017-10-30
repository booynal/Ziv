package com.ziv.test.nio.file;

import org.junit.Test;
import test.junit.base.BaseTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * 1. 创建观察服务(FileSystems)<br/>
 * 2. 注册想要监听的文件(Paths)<br/>
 * 3. 调用take或者poll进行监听(WatchService)<br/>
 * 4. 处理完毕后必须将WatchKey做重置以放回观察队列(WatchEvent)<br/>
 * <p>
 * 测试结果：<br/>
 * <p>
 * 根目录下：
 * <li>创建 -> ENTRY_CREATE</li>
 * <li>删除 -> ENTRY_DELETE</li>
 * <li>修改 -> ENTRY_MODIFY</li>
 * <li>重命名 -> ENTRY_CREATE, ENTRY_DELETE</li>
 * <p>
 * 一级子目录下：
 * <li>创建 -> 其父目录的ENTRY_MODIFY</li>
 * <li>删除 -> 其父目录的ENTRY_MODIFY</li>
 * <li>修改 -> 没有事件</li>
 * <p>
 * 二级子目录下：创建、删除、修改都没有收到事件
 * <p>
 * Created by ziv on 2017/10/30.
 */
public class FileWatchTest extends BaseTest {

	@Test
	public void test() throws IOException {
		try {
			// 1
			WatchService watchService = FileSystems.getDefault().newWatchService();
			// 2
			File file = new File("."); // 项目根目录
			System.out.println("监听路径: " + file.getAbsoluteFile());
			// ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY 这3种模式可以任意组合
			// 经过测试，如果不监听ENTRY_DELETE的话，删除文件就没有触发代码
			WatchKey registerWattchKey = Paths.get(file.toURI()).register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.OVERFLOW);
			System.out.println("registerWattchKey: " + registerWattchKey);
			// 3
			WatchKey watchKey = null;
			while (null != (watchKey = watchService.take())) {
				//System.out.println("watchKey: " + watchKey);
				//System.out.println("watchKey.isValid(): " + watchKey.isValid());
				List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
				System.out.println("收到监听事件，watchEvents: " + watchEvents);
				if (null != watchEvents) {
					for (WatchEvent watchEvent : watchEvents) {
						WatchEvent.Kind kind = watchEvent.kind();
						Object context = watchEvent.context();
						int count = watchEvent.count();
						System.out.println(String.format("kind: '%s', count: '%s', context: '%s'", kind, count, context));
					}
				}
				System.out.println("reset: " + watchKey.reset()); // 必须重置
			}
			System.out.println("end");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
