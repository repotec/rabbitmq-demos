package com.java.rabbitmq.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.Predicate;

import com.java.rabbitmq.message.MessagingException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Envelope;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RabbitMqSubscribe {

	private final String AMQP_QUEUE;
	private final Channel channel;

	public void basicSubscribe() throws MessagingException, IOException {
		
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				long deliveryTag = envelope.getDeliveryTag();

				channel.basicAck(deliveryTag, false);

				String message = new String(body, "UTF-8");
				System.out.println("Consumed: " + message);
			}
		};
		
	}

	public void subscribeAutoAck() throws MessagingException, IOException {	
		boolean autoAck = false;
		
		channel.basicConsume(AMQP_QUEUE, 
							 autoAck,
							"my-consumerTag",
							(consumerTag, delivery) -> {
								String message = convertMessage(delivery.getBody());
								System.out.println("consumerTag:" + consumerTag + "|Consumed: " + message);
							},
			
							consumerTag -> {
								System.out.println("callback|consumerTag:" + consumerTag);
							});
	}

	/**
	 * 
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void subscribeManualAck() throws MessagingException {
		boolean autoAck = false;
		boolean multiple = false;
		try {
			channel.basicConsume(AMQP_QUEUE, 
								autoAck,
								"my-consumerTag",
								(consumerTag, delivery) -> {
									String message = convertMessage(delivery.getBody());
									System.out.println("consumerTag:" + consumerTag + "|Consumed: " + message);
			
									channel.basicAck(delivery.getEnvelope().getDeliveryTag(), multiple);
								},
			
								consumerTag -> {
									System.out.println("callback|consumerTag:" + consumerTag);
								});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void subscribeNegativeAck(SubscriberCallback<String> callbackValidation) throws MessagingException, IOException {
		boolean autoAck = false;
		boolean multiple = false; 	//multiple messages
		boolean requeue = false; 
		
		channel.basicConsume(AMQP_QUEUE, 
				 			 autoAck, 
							"my-consumerTag",
			
							//DeliverCallback
							(consumerTag, delivery) -> { 
								String msg =  new String(delivery.getBody(), "UTF-8");
								System.out.println("consumerTag:" + consumerTag + "|Consumed: " + msg);
								long deliveryTag = delivery.getEnvelope().getDeliveryTag();
								
								if(callbackValidation.check(msg)) {
									System.out.println("DeliverCallback|validation passed|consumerTag:" + consumerTag);
									channel.basicAck(deliveryTag, multiple);
								}else {
									System.out.println("DeliverCallback|validation is not passed|consumerTag:" + consumerTag);
									channel.basicNack(deliveryTag, multiple, requeue);
								}
							},
				
							//CancelCallback
							consumerTag -> {		
								System.out.println("CancelCallback|consumerTag:" + consumerTag);
							});
	}
	
	public String convertMessage(byte[] body) {
		try {
			return new String(body, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
