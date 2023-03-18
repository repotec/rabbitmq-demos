package com.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitSchemaConfig {
	private final String EXCHANGE_NAME = "x.picture.topic";
	@Bean
	public Declarables rabbitSchema() {
		return new Declarables(new TopicExchange("x.picture.topic"),
				
							   new Queue("q.picture.image"),
							   new Binding("q.picture.image", DestinationType.QUEUE , EXCHANGE_NAME, "#.jpg", null),
							   new Binding("q.picture.image", DestinationType.QUEUE , EXCHANGE_NAME, "*.*.png", null),
							   
							   new Queue("q.picture.vector"),
							   new Binding("q.picture.vector", DestinationType.QUEUE , EXCHANGE_NAME, "*.*.svg", null),
							   
							   new Queue("q.picture.mobile"),
							   new Binding("q.picture.vector", DestinationType.QUEUE , EXCHANGE_NAME, "mobile.#", null),
							   
							   new Queue("q.picture.large"),
							   new Binding("q.picture.vector", DestinationType.QUEUE , EXCHANGE_NAME, "*.large.svg", null));
	}
}
