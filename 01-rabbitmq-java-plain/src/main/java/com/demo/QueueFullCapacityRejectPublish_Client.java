package com.demo;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.java.rabbitmq.core.Connector;
import com.java.rabbitmq.core.Publisher;
import com.java.rabbitmq.core.arugments.Arguments;
import com.java.rabbitmq.message.MessagingException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.AMQP.Queue;

public class QueueFullCapacityRejectPublish_Client extends BaseClient {
    private static final String AMQP_EXCHANGE_2 = "exchange_1";
    private static final String AMQP_LIMITED_OVERFLOW_QUEUE = "limited_overflow_queue";
    private static final String AMQP_ROUTUNG_KEY = "";
    
	public static void main(String[] args) throws MessagingException, IOException, InterruptedException {
		Connector connector = new Connector(HOST_NAME, PORT_NUMBER, USERNAME, PASSWORD);
		Channel channel = connector.start();
		
		createSchema(channel);
		
		//publisher 2
		Publisher publisher = new Publisher(AMQP_EXCHANGE_2, AMQP_ROUTUNG_KEY, channel);
		for(int i = 1; i <= 5; i++) {
			String message = "This is a test message sent @ " + new Date();
			publisher.basicPublishConfirmAsync2(message, null);
		}
		
    	latch.await();
    	connector.stop();
		latch.countDown();
	}
	
	public static void createSchema(Channel channel) throws IOException {
		channel.exchangeDeclare(AMQP_EXCHANGE_2, "fanout", false, true, null);
        channel.queueDeclare(AMQP_LIMITED_OVERFLOW_QUEUE, false, false, true, Map.of(Arguments.MAX_LENGTH, 3, 
        																			 Arguments.OVERFLOW, "reject-publish"));
        channel.queueBind(AMQP_LIMITED_OVERFLOW_QUEUE, AMQP_EXCHANGE_2, AMQP_ROUTUNG_KEY);
	}
}
