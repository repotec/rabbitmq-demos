package com.demo;

import java.io.IOException;
import java.util.Date;

import com.java.rabbitmq.core.Connector;
import com.java.rabbitmq.core.Publisher;
import com.java.rabbitmq.message.MessagingException;
import com.rabbitmq.client.Channel;

public class PublisherConfirm_Client extends BaseClient {
    private static final String AMQP_EXCHANGE = "exchange-without-queue";
    
	public static void main(String[] args) throws MessagingException, IOException, InterruptedException {
		Connector connector = new Connector(HOST_NAME, PORT_NUMBER, USERNAME, PASSWORD);
		
		Channel channel = connector.start();
		
		channel.exchangeDeclare(AMQP_EXCHANGE, "fanout", false, false, null);
        
		//publish
		Publisher publisher = new Publisher(AMQP_EXCHANGE, "",channel);
		String message = "This is a test message sent @ " + new Date();
		publisher.basicPublishReturnAsync(message, null);

    	latch.await();
    	connector.stop();
		latch.countDown();
	}
}
