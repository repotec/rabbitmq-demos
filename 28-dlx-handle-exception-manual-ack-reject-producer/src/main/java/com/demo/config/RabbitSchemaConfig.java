package com.demo.config;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitSchemaConfig {
	
	private final String REJECT_EXCHANGE = "x.pictures";
	private final String REJECT_QUEUE = "q.pictures";
	
	private final String DLX_EXCHANGE = "x.pictures-dlx";
	private final String DLX_QUEUE = "q.pictures-dlx";
	
	@Bean
	public Declarables rabbitSchema() {
		return new Declarables(new FanoutExchange(REJECT_EXCHANGE, true, true),
				   			   new FanoutExchange(DLX_EXCHANGE, true, true),
							   new Queue(DLX_QUEUE, true, false, true),
							   new Queue(REJECT_QUEUE, true, false, true, Map.of("x-dead-letter-exchange", DLX_EXCHANGE)),
							   new Binding(REJECT_QUEUE, DestinationType.QUEUE, REJECT_EXCHANGE, "", null),
							   new Binding(DLX_QUEUE, DestinationType.QUEUE, DLX_EXCHANGE, "", null));
	}
}