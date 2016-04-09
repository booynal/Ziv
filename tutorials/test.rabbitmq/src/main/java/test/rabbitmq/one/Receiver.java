package test.rabbitmq.one;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Receiver {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws java.io.IOException, java.lang.InterruptedException, TimeoutException {
		// 1
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		// 2
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		// 3
		Consumer callbackConsumer = new DefaultConsumer(channel) {

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body);
				System.err.println(" [x] Received '" + message + "'");
			}

		};
		// also can use QueueingConsumer
		// QueueingConsumer extends DefaultConsumer
		channel.basicConsume(QUEUE_NAME, true, callbackConsumer);
	}

}
