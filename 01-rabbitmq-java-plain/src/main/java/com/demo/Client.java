package com.demo;

import java.util.Date;
import java.util.HashMap;

import com.demo.event.message.MessageHandler;
import com.demo.event.message.MessagingException;

public class Client {
    // MUST replace this URL to your instance on RabbitMQ
    private static final String HOST_NAME = "172.29.154.144";
    private static final String PORT_NUMBER = "5672";
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";
    
    
    private static final String AMQP_EXCHANGE = "acme.sales.topic";
    private static final String AMQP_TOPIC = "acme.sales.bookingconfirmed";
    private static final String AMQP_TOPIC_ROUTUNG_KEY = "K-100";
    
	public static void main(String[] args) throws Exception {

		// 1. Create and instance of the PubSubService
		HashMap<String, String> props = setupProperties();
		PubSubService pubSubService = new PubSubService(props);

		// 2. Start the connection
		pubSubService.start();

		String message = "This is a test message sent @ " + new Date();

		// 3. Test the publish
		   pubSubService.publish(message);

		// 4. Test the subscribe
		//subscribeTest(pubSubService);

		System.out.println("Press any key to end the test..");
		System.in.read();

		// Stop the connection
		pubSubService.stop();

	}

	private static void subscribeTest(PubSubService pubSubService) {

		// Handler for the message
		MessageHandler messageHandler = (message) -> {
			System.out.println(message);
		};

		try {
			pubSubService.subscribe(messageHandler);
		} catch (MessagingException me) {
			me.printStackTrace();
		}

	}

	private static HashMap<String, String> setupProperties() {
		HashMap<String, String> props = new HashMap<>();
		props.put("HOST_NAME", HOST_NAME);
		props.put("PORT_NUMBER", PORT_NUMBER);
		props.put("USERNAME", USERNAME);
		props.put("PASSWORD", PASSWORD);
	    
		props.put("AMQP_EXCHANGE", AMQP_EXCHANGE);
		props.put("AMQP_TOPIC", AMQP_TOPIC);
		props.put("AMQP_TOPIC_ROUTUNG_KEY", AMQP_TOPIC_ROUTUNG_KEY);
		return props;
	}
}
