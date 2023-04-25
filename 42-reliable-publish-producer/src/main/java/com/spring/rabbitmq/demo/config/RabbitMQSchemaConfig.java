package com.spring.rabbitmq.demo.config;

import java.util.Map;

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

	private final String X_HR = "x.hr";
	private final String Q_HR = "q.hr";
	
	@Bean
	public Declarables rabbitSchema() {
		log.info("start configure RabbitMQ schema using delcarables...");
		
		return new Declarables(new FanoutExchange(X_HR, true, true, Map.of("x-single-active-consumer", "true")),
							   new Queue(Q_HR,  true, false, true, null),
							   new Binding(Q_HR, DestinationType.QUEUE, X_HR, "", null));
	}
}