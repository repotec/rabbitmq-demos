package com.demo;

import java.io.IOException;
import java.util.Date;

import com.java.rabbitmq.core.Connector;
import com.java.rabbitmq.core.Publisher;
import com.java.rabbitmq.core.Subscriber;
import com.rabbitmq.client.Channel;

public class SubscribeManualPositiveAck_Client extends BaseClient {
    
    private static final String AMQP_EXCHANGE = "exchange_1";
    private static final String AMQP_QUEUE = "queue_1";
    private static final String AMQP_ROUTUNG_KEY = "K-100";
    
	public static void main(String[] args) throws Exception {
		Connector connector = new Connector(HOST_NAME, PORT_NUMBER, USERNAME, PASSWORD);
		Channel channel = connector.start();
		
		createSchema(channel);

		//publisher
		Publisher publisher = new Publisher(AMQP_EXCHANGE, AMQP_ROUTUNG_KEY, channel);
		String message = "This is a test message sent @ " + new Date();
		publisher.basicPublish(message, null);
  
		//subscriber
    	Subscriber subscriber = new Subscriber(AMQP_QUEUE, channel);
    	subscriber.subscribeManualAck();
    	
    	latch.await();
		connector.stop();
		latch.countDown();
	}
	
	public static void createSchema(Channel channel) throws IOException {
		channel.exchangeDeclare(AMQP_EXCHANGE, "fanout", false, true, null);
        channel.queueDeclare(AMQP_QUEUE, false, true, false, null);
        channel.queueBind(AMQP_QUEUE, AMQP_EXCHANGE, AMQP_ROUTUNG_KEY);
	}
}
