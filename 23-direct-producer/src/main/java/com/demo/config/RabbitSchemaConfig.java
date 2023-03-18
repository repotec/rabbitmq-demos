package com.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitSchemaConfig {
	
	@Bean
	public Declarables rabbitSchema() {
		return new Declarables(new DirectExchange("x.picture"),
							   new Queue("q.picture.image"),
							   new Binding("q.picture.image", DestinationType.QUEUE , "x.picture", "jpg", null),
							   new Binding("q.picture.image", DestinationType.QUEUE , "x.picture", "png", null),
							   new Queue("q.picture.vector"),
							   new Binding("q.picture.vector", DestinationType.QUEUE , "x.picture", "svg", null));
	}
}
