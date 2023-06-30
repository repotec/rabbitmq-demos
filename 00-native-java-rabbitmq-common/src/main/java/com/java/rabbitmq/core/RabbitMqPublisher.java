package com.java.rabbitmq.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.ReturnListener;

import lombok.RequiredArgsConstructor;

import com.java.rabbitmq.message.MessagingException;

@RequiredArgsConstructor
public class RabbitMqPublisher {
	
	private final String AMQP_EXCHANGE;
	private final String AMQP_ROUTUNG_KEY;
	private final Channel channel;
	
	public void basicPublish(String data) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}

		byte[] bytes = null;
		try {
			bytes = data.getBytes("UTF-8");

			channel.basicPublish(AMQP_EXCHANGE, AMQP_ROUTUNG_KEY, null, bytes);
		} catch (UnsupportedEncodingException uese) {
			throw new MessagingException("Data could not be converted to UTF-8!!", uese);
		} catch (IOException ioe) {
			throw new MessagingException("Could not publish to RabbitMQ!!", ioe);
		}

	}
	
	public void basicPublishMandatorySync(String data) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}
		
		boolean mandatory = true;

		try {
			byte[] bytes = data.getBytes("UTF-8");

			channel.basicPublish(AMQP_EXCHANGE, AMQP_ROUTUNG_KEY, mandatory, null, bytes);
			try {
				channel.waitForConfirmsOrDie(1000L);
			} catch (InterruptedException | TimeoutException e) {
				e.printStackTrace();
			}
		} catch (IOException ioe) {
			throw new MessagingException("Could not publish to RabbitMQ!!", ioe);
		}
	}
	
	public void basicPublishMandatoryAsync(String message) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}
		
		boolean mandatory = true;

		try {
			channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> {
		    	if(replyText.equals("NO_ROUTE")) 
		    		System.out.println("message to " + exchange + " is not routable|replyCode:" + replyCode);
		    	else
		    		System.out.println("message to " + exchange + " is not routable|replyCode:" + replyCode);
		    	
			});
			
			channel.basicPublish(AMQP_EXCHANGE, AMQP_ROUTUNG_KEY, mandatory, null, message.getBytes("UTF-8"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void basicPublishMandatoryTransAsync(String message) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}
		
		boolean mandatory = true;

		channel.addConfirmListener(new ConfirmListener() {
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("message to rejected");
			}

			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("Message(s) confirmed.");
			}
		});

		try {
			channel.confirmSelect();
			channel.basicPublish(AMQP_EXCHANGE, AMQP_ROUTUNG_KEY, true, null, message.getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
