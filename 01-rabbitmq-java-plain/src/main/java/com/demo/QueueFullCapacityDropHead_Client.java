package com.demo;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.java.rabbitmq.core.Connector;
import com.java.rabbitmq.core.Publisher;
import com.java.rabbitmq.core.arugments.Arguments;
import com.java.rabbitmq.message.MessagingException;
import com.rabbitmq.client.Channel;

public class QueueFullCapacityDropHead_Client extends BaseClient {
    private static final String AMQP_EXCHANGE = "exchange_1";
    private static final String AMQP_QUEUE = "limited_queue";
    private static final String AMQP_ROUTUNG_KEY = "";
    
	public static void main(String[] args) throws MessagingException, IOException, InterruptedException {
		Connector connector = new Connector(HOST_NAME, PORT_NUMBER, USERNAME, PASSWORD);
		Channel channel = connector.start();
		
		createSchema(channel);
		
		Publisher publisher1 = new Publisher(AMQP_EXCHANGE, AMQP_ROUTUNG_KEY, channel);
		for(int i = 1; i <= 5; i++) {
			String message = "This is a test message sent @ " + new Date();
			publisher1.basicPublishConfirmAsync2(message, null);
		}
		
		
    	latch.await();
    	connector.stop();
		latch.countDown();
	}
	
	public static void createSchema(Channel channel) throws IOException {
		channel.exchangeDeclare(AMQP_EXCHANGE, "fanout", false, true, null);		
        channel.queueDeclare(AMQP_QUEUE, false, false, true, Map.of(Arguments.MAX_LENGTH, 3));
        channel.queueBind(AMQP_QUEUE, AMQP_EXCHANGE, AMQP_ROUTUNG_KEY);
	}
}
