package com.demo;

import java.io.IOException;
import java.util.Date;

import com.java.rabbitmq.core.Connector;
import com.java.rabbitmq.core.Publisher;
import com.java.rabbitmq.message.MessagingException;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

public class durableMessage_Client extends BaseClient {
	private static final String AMQP_EXCHANGE = "x-durable";
	private static final String AMQP_QUEUE = "q-durable";

	public static void main(String[] args) throws MessagingException, IOException, InterruptedException {
		Connector connector = new Connector(HOST_NAME, PORT_NUMBER, USERNAME, PASSWORD);
		Channel channel = connector.start();

		createSchema(channel);
		
		// publish
		Publisher publisher = new Publisher(AMQP_EXCHANGE, "", channel);
		String message = "This is a test message sent @ " + new Date();
		BasicProperties properties = new BasicProperties.Builder().deliveryMode(2).build();
	
		publisher.basicPublish(message, properties);
		publisher.basicPublish(message, null);
		
		latch.await();
		connector.stop();
		latch.countDown();
	}
	
	public static void createSchema(Channel channel) throws IOException {
		channel.exchangeDeclare(AMQP_EXCHANGE, "fanout", true, true, null);		
        channel.queueDeclare(AMQP_QUEUE, true, false, true, null);
        channel.queueBind(AMQP_QUEUE, AMQP_EXCHANGE, "");
	}
}
