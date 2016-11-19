package killsignal;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * Signal在java8中才有
 *
 * @author ziv
 *
 */
@SuppressWarnings("restriction")
public class Application {

	public static void main(String[] args) throws InterruptedException {
		SignalHandler signalHandler = new SignalHandler() {

			@Override
			public void handle(Signal signal) {
				System.out.println("SignalHandler: " + signal);
			}
		};
		// 只有kill -9能够结束jvm进程，别的信号量只是发送给java进程处理，至于如何响应是程序代码决定的
		Signal.handle(new Signal("HUP"), signalHandler); // kill -1 PID
		Signal.handle(new Signal("INT"), signalHandler); // kill -2 PID
		// already used by VM or OS: SIGQUIT
		// Signal.handle(new Signal("QUIT"), signalHandler); // kill -3 PID
		Signal.handle(new Signal("ABRT"), signalHandler); // kill -6 PID
		// already used by VM or OS: SIGKILL
		// Signal.handle(new Signal("KILL"), signalHandler); // kill -9 PID
		Signal.handle(new Signal("ALRM"), signalHandler); // kill -14 PID
		Signal.handle(new Signal("TERM"), signalHandler); // kill -15 PID
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				System.err.println("Runtime shutdown hook");
			}
		});

		for (int i = 0; i < 100; i++) {
			Thread.sleep(1000);
			System.out.println(i);
		}
	}

}
