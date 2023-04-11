package com.spring.rebbitmq.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitSchemaConfig {
	public static final String EXCHANGE = "x.invoice";
	public static final String QUEUE_ONE = "q.invoice.one";
	public static final String QUEUE_TWO = "q.invoice.two";
	
	@Bean
	public Declarables rabbitSchema() {
		return new Declarables(new CustomExchange(EXCHANGE, "x-consistent-hash", false, true) ,
				          	   new Queue("q.invoice.one"),
							   new Binding("q.invoice.one", DestinationType.QUEUE , "x.invoice", "10", null),
							   new Queue("q.invoice.two"),
							   new Binding("q.invoice.two", DestinationType.QUEUE , "x.invoice", "5", null));
	}
}
