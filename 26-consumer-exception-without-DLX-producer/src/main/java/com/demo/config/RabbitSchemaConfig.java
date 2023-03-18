package com.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitSchemaConfig {
	
	private final String EXCHANGE_NAME = "x.consumer-exception-without-dlx";
	private final String QUEUE_NAME = "q.consumer-exception-without-dlx";
	
	@Bean
	public Declarables rabbitSchema() {
		return new Declarables(new FanoutExchange(EXCHANGE_NAME, true, true),
				   new Queue(QUEUE_NAME, true, false, true),
				   new Binding(QUEUE_NAME, DestinationType.QUEUE, EXCHANGE_NAME, "", null));
	}
	
	
}