package com.demo;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.retry.backoff.Sleeper;

import com.java.rabbitmq.core.RabbitMqConnection;
import com.java.rabbitmq.core.RabbitMqPublisher;
import com.java.rabbitmq.core.RabbitMqSubscribe;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class SubscribeNegativeAckClient extends Client {
    private static final String HOST_NAME = "localhost";
    private static final String PORT_NUMBER = "5672";
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";
    
	private static final String X_WORK = "x.work";
	private static final String X_WAIT = "x.wait";
	private static final String X_DEAD = "x.dead";
	
	private static final String Q_IMAGE_WORK = "q.image.work";
	private static final String Q_IMAGE_WAIT = "q.image.wait";
	private static final String Q_IMAGE_DEAD = "q.image.dead";

	
	private static Channel channel = null;
	
	public static void config() throws IOException {
		channel.exchangeDeclare(X_WORK, "fanout", false, true, null);
		channel.exchangeDeclare(X_DEAD, "fanout", false, true, null);
		
        channel.queueDeclare(Q_IMAGE_WORK, false, false, true, Map.of("x-dead-letter-exchange", X_DEAD));
        channel.queueDeclare(Q_IMAGE_DEAD, false, false, true, null);
        
        channel.queueBind(Q_IMAGE_WORK, X_WORK, "");
        channel.queueBind(Q_IMAGE_DEAD, X_DEAD, "");
	}
    
	public static void main(String[] args) throws Exception {

		RabbitMqConnection connection = new RabbitMqConnection(HOST_NAME, PORT_NUMBER, USERNAME, PASSWORD);
		
		channel = connection.start();
		
		config();
		
		
		RabbitMqPublisher publisher = new RabbitMqPublisher(X_WORK, "", channel);
		String message = "This is a test message sent @ " + new Date();
		publisher.basicPublish(message);
	
    	RabbitMqSubscribe pubSubService = new RabbitMqSubscribe(Q_IMAGE_WORK, channel);
    	
    	
    	pubSubService.subscribeNegativeAck((msg) -> !msg.contains("@"));
    	latch.await();
    	
		connection.stop();
		
		latch.countDown();
	}
}
