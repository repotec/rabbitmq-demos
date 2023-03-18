package com.demo;

import com.demo.event.message.MessageHandler;
import com.demo.event.message.MessagingException;
import com.demo.event.message.MessagingService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.AMQP.Exchange;
import com.rabbitmq.client.AMQP.Queue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;

public class PubSubService implements MessagingService {
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

	/**
	 *
	 * @param props contain all of the MQ specific properties
	 */
	public PubSubService(HashMap<String, String> props) {
		HOST_NAME = props.get("HOST_NAME");
		PORT_NUMBER = props.get("PORT_NUMBER");
		USERNAME = props.get("USERNAME");
		PASSWORD = props.get("PASSWORD");
		
		
		// Exchange
		AMQP_EXCHANGE = props.get("AMQP_EXCHANGE");

		// this is the routing key in Rabbit MQ terminology
		AMQP_TOPIC = props.get("AMQP_TOPIC");
		
		//routing key for topic queue
		AMQP_TOPIC_ROUTUNG_KEY =  props.get("AMQP_TOPIC_ROUTUNG_KEY");
	}

	/**
	 * This creates the connection to Rabbit MQ
	 *
	 * As per recommendation, keep the connection and channel open for reuse
	 * https://www.rabbitmq.com/api-guide.html#connection-and-channel-lifspan
	 */
	@Override
	public void start() throws MessagingException {
		// If the channel is already open then throw an exception
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

	/**
	 *
	 * @param data String data to be published
	 * @throws MessagingException
	 *
	 */
	@Override
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

	/**
	 * Blocking function
	 * 
	 * @param handler - for handling the message received
	 *
	 *                https://www.rabbitmq.com/api-guide.html#exchanges-and-queues
	 */
	@Override
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

	/**
	 *
	 * @param handler   object for handling the message
	 * @param queueName common queue that may be consumed with multipe handlers
	 * @throws MessagingException
	 *
	 *                            https://www.rabbitmq.com/api-guide.html#exchanges-and-queues
	 */
	@Override
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
			});

		} catch (IOException ioe) {
			throw new MessagingException("RabbitMQ subscription error !!!", ioe);
		}
	}

	/**
	 * Stops the connection | channel
	 * 
	 * @throws MessagingException
	 *
	 *                            https://www.rabbitmq.com/api-guide.html#shutdown
	 */
	@Override
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
	
	private void createOrReplace(Channel channel, String exchangeName, String exchangeType, String queueName, String routingKey) throws IOException {
	   
//	    	Exchange.DeclareOk ok = channel.exchangeDeclarePassive(exchangeName);
//	    
//	    	if(ok.)
//	        channel.queueDeclarePassive(queueName);
	    	
	        channel.exchangeDeclare(exchangeName, exchangeType, false, true, null);
	        channel.queueDeclare(queueName, true, false, false, null);
	        channel.queueDeclare(queueName, true, false, false, null);
	        channel.queueBind(queueName, exchangeName, routingKey);
	    
	}
}
