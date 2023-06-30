package com.demo;

import java.io.IOException;
import java.util.Date;

import com.java.rabbitmq.core.RabbitMqConnection;
import com.java.rabbitmq.core.RabbitMqPublisher;
import com.java.rabbitmq.core.RabbitMqSubscribe;
import com.java.rabbitmq.message.MessagingException;
import com.rabbitmq.client.Channel;

public class PublisherConfirmClient extends Client {
    private static final String AMQP_EXCHANGE = "exchange-without-queue";
    private static final String AMQP_QUEUE = "unroutable-queue";
    
	public static void main(String[] args) throws MessagingException, IOException, InterruptedException {
		RabbitMqConnection connection = new RabbitMqConnection(HOST_NAME, PORT_NUMBER, USERNAME, PASSWORD);
		
		Channel channel = connection.start();
		
		channel.exchangeDeclare(AMQP_EXCHANGE, "fanout");
        
		//publish
		RabbitMqPublisher publisher = new RabbitMqPublisher(AMQP_EXCHANGE, "",channel);
		String message = "This is a test message sent @ " + new Date();
		publisher.basicPublishMandatoryTransAsync(message);


    	latch.await();
		channel.abort();
    	connection.stop();
		
		latch.countDown();
	}
}
