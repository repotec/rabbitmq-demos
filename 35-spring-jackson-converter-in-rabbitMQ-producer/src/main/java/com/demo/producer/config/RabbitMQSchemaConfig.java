package com.demo.producer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 *  @author ahmed
 * 
 */
@Configuration
public class RabbitMQSchemaConfig {
	public static final Logger log = LoggerFactory.getLogger(RabbitMQSchemaConfig.class);

	private final String X_WORK = "x.work";
	private final String Q_WORK = "q.work";

	@Bean
	public Declarables rabbitSchema() {
		log.info("start configure RabbitMQ schema using delcarables...");
		
		return new Declarables(new FanoutExchange(X_WORK, true, true),
							   new Queue(Q_WORK,  true, false, true, null),
							   new Binding(Q_WORK, DestinationType.QUEUE, X_WORK, "", null)

				);
	}
}