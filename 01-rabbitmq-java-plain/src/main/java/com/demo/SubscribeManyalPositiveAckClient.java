package com.demo;

import java.util.Date;
import java.util.HashMap;

import com.java.rabbitmq.core.ConsumerUtil;
import com.java.rabbitmq.core.PubSubService;
import com.java.rabbitmq.core.RabbitMqConnection;
import com.java.rabbitmq.core.RabbitMqPublisher;
import com.java.rabbitmq.core.RabbitMqSubscribe;
import com.java.rabbitmq.message.MessageHandler;
import com.java.rabbitmq.message.MessagingException;
import com.rabbitmq.client.Channel;

public class SubscribeManyalPositiveAckClient extends Client {
    
    private static final String AMQP_EXCHANGE = "exchange_1";
    private static final String AMQP_QUEUE = "queue_1";
    private static final String AMQP_ROUTUNG_KEY = "K-100";
    
	public static void main(String[] args) throws Exception {

		RabbitMqConnection connection = new RabbitMqConnection(HOST_NAME, PORT_NUMBER, USERNAME, PASSWORD);
		
		Channel channel = connection.start();
		
		channel.exchangeDeclare(AMQP_EXCHANGE, "topic", false, true, null);
        channel.queueDeclare(AMQP_QUEUE, false, true, false, null);
        channel.queueDeclare(AMQP_QUEUE, false, true, false, null);
        channel.queueBind(AMQP_QUEUE, AMQP_EXCHANGE, AMQP_ROUTUNG_KEY);
        
		RabbitMqPublisher publisher = new RabbitMqPublisher(AMQP_EXCHANGE, 
															AMQP_ROUTUNG_KEY,
															channel);
		
		String message = "This is a test message sent @ " + new Date();
		//publisher.basicPublish(message);
		publisher.basicPublishMandatoryAsync(message);
  
    	RabbitMqSubscribe pubSubService = new RabbitMqSubscribe(AMQP_QUEUE, channel);
    	pubSubService.subscribeManualAck();
    	
    	latch.await();
    	
		connection.stop();
		
		latch.countDown();
	}
}
