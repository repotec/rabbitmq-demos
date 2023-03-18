package com.demo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitSchemaConfig {
	
	@Bean
	public Declarables rabbitSchema() {
		return new Declarables(new FanoutExchange("x.fixed-rate"),
							   new Queue("q.fixed-rate", true, false, false, null),
							   new Binding("q.fixed-rate", DestinationType.QUEUE, "x.fixed-rate", "", null));
	}
}