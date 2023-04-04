package com.spring.rebbitmq.demo.config;

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

	private final String X_INVOICE = "x.invoice";
	private final String Q_INVOICE = "q.invoice";
	
	@Bean
	public Declarables rabbitSchema() {
		log.info("start configure RabbitMQ schema using delcarables...");
		
		return new Declarables(new FanoutExchange(X_INVOICE, true, true),
							   new Queue(Q_INVOICE,  true, false, true, null),
							   new Binding(Q_INVOICE, DestinationType.QUEUE, X_INVOICE, "", null)

				);
	}
}