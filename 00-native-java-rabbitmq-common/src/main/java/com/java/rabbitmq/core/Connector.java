package com.java.rabbitmq.core;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.java.rabbitmq.message.MessagingException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Connector {
	private final String HOST_NAME;
	private final String PORT_NUMBER;
	private final String USERNAME;
	private final String PASSWORD;
	
	private ConnectionFactory connectionFactory;
	private Connection connection = null;
	private Channel channel = null;
	
	public Channel start() throws MessagingException {
		if (channel != null && channel.isOpen()) {
			throw new MessagingException("Connection to MQ already open!!!");
		}
		connectionFactory = new ConnectionFactory();
		connectionFactory.setUsername(USERNAME);
		connectionFactory.setPassword(PASSWORD);
		connectionFactory.setHost(HOST_NAME);
		connectionFactory.setPort(Integer.parseInt(PORT_NUMBER));
		
		try {
			connection = connectionFactory.newConnection();
			channel = connection.createChannel();
		} catch (Exception e) {
			throw new MessagingException("Error creating connection/channel for RabbitMQ!!", e);
		}
		
		return channel;
	}
	
	
	public void stop() throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}

		try {
			channel.close();
			connection.close();
		} catch (IOException ioe) {
			throw new MessagingException("Error in closing connection/channel!!", ioe);
		} catch (TimeoutException toe) {
			throw new MessagingException("Error in closing connection/channel!!", toe);
		}
	}
}
