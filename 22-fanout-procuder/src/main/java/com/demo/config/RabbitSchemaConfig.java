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
	
	@Bean
	public Declarables rabbitSchema() {
		System.out.println("RabbitSchemaConfig");
		return new Declarables(new FanoutExchange("x.hr"),
							   new Queue("q.hr.accounting", true, false, false, null),
							   new Queue("q.hr.marketing", true, false, false, null),
							   new Binding("q.hr.accounting", DestinationType.QUEUE, "x.hr", "", null),
							   new Binding("q.hr.marketing", DestinationType.QUEUE, "x.hr", "", null));
	}
}