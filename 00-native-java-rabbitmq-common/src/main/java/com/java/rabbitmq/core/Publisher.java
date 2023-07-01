package com.java.rabbitmq.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

import com.java.rabbitmq.message.MessagingException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.ConfirmListener;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Publisher  {
	
	private final String AMQP_EXCHANGE;
	private final String AMQP_ROUTUNG_KEY;
	private final Channel channel;
	
	public void basicPublish(String data, BasicProperties basicProperties) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}

		byte[] bytes = null;
		try {
			bytes = data.getBytes("UTF-8");

			channel.basicPublish(AMQP_EXCHANGE, AMQP_ROUTUNG_KEY, basicProperties, bytes);
		} catch (UnsupportedEncodingException uese) {
			throw new MessagingException("Data could not be converted to UTF-8!!", uese);
		} catch (IOException ioe) {
			throw new MessagingException("Could not publish to RabbitMQ!!", ioe);
		}
	}
	
	public void basicPublishSync(String data, BasicProperties basicProperties) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}
		
		boolean mandatory = true;

		try {
			try {
				channel.confirmSelect();
				channel.waitForConfirmsOrDie(1000L);
				
				channel.basicPublish(AMQP_EXCHANGE, AMQP_ROUTUNG_KEY, mandatory, basicProperties, data.getBytes("UTF-8"));
			} catch (InterruptedException | TimeoutException e) {
				e.printStackTrace();
			}
		} catch (IOException ioe) {
			throw new MessagingException("Could not publish to RabbitMQ!!", ioe);
		}
	}
	
	public void basicPublishReturnAsync(String message, BasicProperties basicProperties) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}
		
		boolean mandatory = true;
		
		try {
			// will fire only if the message is not routable for any kind of reasons
			channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> {
				System.out.println("ReturnListener:" + replyText);
		    	if(replyText.equals("NO_ROUTE")) 
		    		System.out.println("message to " + exchange + " is not routable|replyCode:" + replyCode);
		    	else
		    		System.out.println("message to " + exchange + " is not routable|replyCode:" + replyCode);
		    	
			});
			
			channel.basicPublish(AMQP_EXCHANGE, AMQP_ROUTUNG_KEY, mandatory, basicProperties, message.getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
		
	public void basicPublishConfirmAsync(String message, BasicProperties basicProperties) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}
		
		boolean mandatory = true;
		
		try {
			// will return confirmed what ever message is routable or not
			AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();
			
			channel.addConfirmListener(new ConfirmListener() {
				public void handleNack(long deliveryTag, boolean multiple) throws IOException {
					System.out.println("addConfirmListener|handleNack|message to rejected|deliveryTag:" + deliveryTag + "|multiple:" + multiple);
				}
	
				public void handleAck(long deliveryTag, boolean multiple) throws IOException {
					System.out.println("addConfirmListener|handleAck|Message(s) confirmed|deliveryTag:" + deliveryTag + "|multiple:" + multiple);
				}
			});
			
			channel.basicPublish(AMQP_EXCHANGE, AMQP_ROUTUNG_KEY, mandatory, basicProperties, message.getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	public void basicPublishConfirmAsync2(String message, BasicProperties basicProperties) throws MessagingException {
		if (channel == null && !channel.isOpen()) {
			throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
		}
		
		boolean mandatory = true;
		
		try {
			// will return confirmed what ever message is routable or not
			AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();

			ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
			
			 ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {
                if (multiple) {
                    ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(sequenceNumber, true);
                    confirmed.clear();
                } else {
                    outstandingConfirms.remove(sequenceNumber);
                }
            };
            
            //it is up to us what will do with this error
            channel.addConfirmListener(cleanOutstandingConfirms, (sequenceNumber, multiple) -> {
                String body = outstandingConfirms.get(sequenceNumber);
                System.err.format("Message with body %s has been nack-ed. Sequence number: %d, multiple: %b%n",body, sequenceNumber, multiple);
                cleanOutstandingConfirms.handle(sequenceNumber, multiple);
            });
            
            
            //for un-routable messages
            channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> {
				System.out.println("ReturnListener:" + replyText);
		    	if(replyText.equals("NO_ROUTE")) 
		    		System.out.println("message to " + exchange + " is not routable|replyCode:" + replyCode);
		    	else
		    		System.out.println("message to " + exchange + " is not routable|replyCode:" + replyCode);
		    	
			});
	            
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
			channel.basicPublish(AMQP_EXCHANGE, AMQP_ROUTUNG_KEY, mandatory, basicProperties, message.getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
