package com.demo.config;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitSchemaConfig {
	
	private final String MAIN_EXCHANGE = "x.pictures";
	private final String WEB_QUEUE = "q.pictures-web";
	private final String MOBILE_QUEUE = "q.pictures-mobile";
	
	private final String DLX_EXCHANGE = "x.pictures-dlx";
	private final String DLX_QUEUE = "q.pictures-dlx";
	
	/*
	 * Scenario:
	 * Messages will spread to two queues (WEB_QUEUE and MOBILE_QUEUE) through direct exchange
	 * Failed or non-consumed will be re-published to dead letter exchange
	 * 
	 * We have two cases:
	 * first case:  The consumer will raise an exception if the image size in more than 9000 KB, then will send the message to DLX queue.
	 * second case: There is no consumer-client to consume the message for any reason, which lead to republish the message to DLX queue automatically.
	 */
	@Bean
	public Declarables rabbitSchema() {
		return new Declarables(new DirectExchange(MAIN_EXCHANGE, false, true),
				   			   new FanoutExchange(DLX_EXCHANGE, false, true),
				   			
							   new Queue(DLX_QUEUE, false, false, true),
							   new Queue(WEB_QUEUE, false, false, true, Map.of("x-dead-letter-exchange", DLX_EXCHANGE)),
							   new Queue(MOBILE_QUEUE,  false, false, true, Map.of("x-dead-letter-exchange", DLX_EXCHANGE, "x-message-ttl", 6000)),
							   
							   new Binding(WEB_QUEUE, DestinationType.QUEUE, MAIN_EXCHANGE, "web", null),
							   new Binding(MOBILE_QUEUE, DestinationType.QUEUE, MAIN_EXCHANGE, "mobile", null),
							   new Binding(DLX_QUEUE, DestinationType.QUEUE, DLX_EXCHANGE, "", null));
	}
}