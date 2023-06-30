package com.java.rabbitmq.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import com.java.rabbitmq.message.MessageHandler;
import com.java.rabbitmq.message.MessagingException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ConsumerUtil {
	private final String HOST_NAME;
	private final String PORT_NUMBER;
	private final String USERNAME;
	private final String PASSWORD;

	public final String AMQP_EXCHANGE;
	public final String AMQP_TOPIC;
	private final String AMQP_TOPIC_ROUTUNG_KEY;

	private ConnectionFactory connectionFactory;
	private Connection connection = null;
	private Channel channel = null;

	public void start() throws MessagingException {
		if (channel != null && channel.isOpen()) {
			throw new MessagingException("Connection to MQ already open!!!");
		}
		connectionFactory = new ConnectionFactory();

		try {
			connectionFactory.setUsername(USERNAME);
			connectionFactory.setPassword(PASSWORD);
			connectionFactory.setHost(HOST_NAME);
			connectionFactory.setPort(Integer.parseInt(PORT_NUMBER));

			connection = connectionFactory.newConnection();
			channel = connection.createChannel();
		} catch (Exception e) {
			throw new MessagingException("Error creating connection/channel for RabbitMQ!!", e);
		}
	}

	public void publish(String data) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}

		byte[] bytes = null;
		try {
			bytes = data.getBytes("UTF-8");

			createOrReplace(channel, AMQP_EXCHANGE, "topic", AMQP_TOPIC, AMQP_TOPIC_ROUTUNG_KEY);

			channel.basicPublish(AMQP_EXCHANGE, AMQP_TOPIC_ROUTUNG_KEY, null, bytes);
		} catch (UnsupportedEncodingException uese) {
			throw new MessagingException("Data could not be converted to UTF-8!!", uese);
		} catch (IOException ioe) {
			throw new MessagingException("Could not publish to RabbitMQ!!", ioe);
		}

	}

	public void publishAutoAck(String data) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}

		byte[] bytes = null;
		try {
			bytes = data.getBytes("UTF-8");

			createOrReplace(channel, AMQP_EXCHANGE, "topic", AMQP_TOPIC, AMQP_TOPIC_ROUTUNG_KEY);

			channel.basicPublish(AMQP_EXCHANGE, AMQP_TOPIC_ROUTUNG_KEY, null, bytes);
		} catch (UnsupportedEncodingException uese) {
			throw new MessagingException("Data could not be converted to UTF-8!!", uese);
		} catch (IOException ioe) {
			throw new MessagingException("Could not publish to RabbitMQ!!", ioe);
		}
	}


	public void publishManualPositiveAck(String data) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}

		byte[] bytes = null;
		try {
			bytes = data.getBytes("UTF-8");

			createOrReplace(channel, AMQP_EXCHANGE, "topic", AMQP_TOPIC, AMQP_TOPIC_ROUTUNG_KEY);

			channel.basicPublish(AMQP_EXCHANGE, AMQP_TOPIC_ROUTUNG_KEY, null, bytes);
		} catch (UnsupportedEncodingException uese) {
			throw new MessagingException("Data could not be converted to UTF-8!!", uese);
		} catch (IOException ioe) {
			throw new MessagingException("Could not publish to RabbitMQ!!", ioe);
		}
	}


	public void subscribe(MessageHandler handler) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}

		try {
			String queueName = channel.queueDeclare().getQueue();
			subscribe(handler, queueName);
		} catch (IOException ioe) {
			throw new MessagingException("Error in queue declaration!!", ioe);
		}
	}

	public void subscribe(MessageHandler handler, String queueName) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}

		try {
			channel.queueBind(queueName, AMQP_EXCHANGE, AMQP_TOPIC_ROUTUNG_KEY);

			// Callback function
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				handler.handleMessage(message);
			};

			channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
				System.out.println("callback|consumerTag:" + consumerTag);
			});

		} catch (IOException ioe) {
			throw new MessagingException("RabbitMQ subscription error !!!", ioe);
		}
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

	private void createOrReplace(Channel channel, String exchangeName, String exchangeType, String queueName,
			String routingKey) throws IOException {
		channel.exchangeDeclare(exchangeName, exchangeType, false, true, null);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);
	}
}
