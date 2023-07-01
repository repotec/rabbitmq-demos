package com.demo;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.java.rabbitmq.core.Connector;
import com.java.rabbitmq.core.Publisher;
import com.java.rabbitmq.core.Subscriber;
import com.java.rabbitmq.core.arugments.Arguments;
import com.rabbitmq.client.Channel;

public class SubscribeNegativeAck_Client extends BaseClient {
	private static final String X_WORK = "x.work";
	private static final String X_DEAD = "x.dead";
	private static final String Q_IMAGE_WORK = "q.image.work";
	private static final String Q_IMAGE_DEAD = "q.image.dead";
    
	public static void main(String[] args) throws Exception {

		Connector connection = new Connector(HOST_NAME, PORT_NUMBER, USERNAME, PASSWORD);
		
		Channel channel = connection.start();
		
		createSchema(channel);
		
		//publisher
		Publisher publisher = new Publisher(X_WORK, "", channel);
		String message = "This is a test message sent @ " + new Date();
		publisher.basicPublish(message, null);
	
		//subscriber
    	Subscriber subscriber = new Subscriber(Q_IMAGE_WORK, channel);
    	subscriber.subscribeNegativeAck((msg) -> !msg.contains("@"));
    	
    	latch.await();
    	
		connection.stop();
		
		latch.countDown();
	}
	
	public static void createSchema(Channel channel) throws IOException {
		channel.exchangeDeclare(X_WORK, "fanout", false, true, null);
		channel.exchangeDeclare(X_DEAD, "fanout", false, true, null);
		
        channel.queueDeclare(Q_IMAGE_WORK, false, false, true, Map.of(Arguments.DLX, X_DEAD));
        channel.queueDeclare(Q_IMAGE_DEAD, false, false, true, null);
        
        channel.queueBind(Q_IMAGE_WORK, X_WORK, "");
        channel.queueBind(Q_IMAGE_DEAD, X_DEAD, "");
	}
}
