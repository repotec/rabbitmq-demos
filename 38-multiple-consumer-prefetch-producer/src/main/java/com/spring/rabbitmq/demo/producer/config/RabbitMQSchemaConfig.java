package com.spring.rabbitmq.demo.producer.config;

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

	private final String X_TRANSACTIONS = "x.transactions";
	private final String X_OFFLINE_REPORTS = "x.offline-reports";
	private final String Q_TRANSACTIONS = "q.transactions";
	private final String Q_OFFLINE_REPORTS = "q.offline-reports";
	
	@Bean
	public Declarables rabbitSchema() {
		log.info("start configure RabbitMQ schema using delcarables...");
		
		return new Declarables(new FanoutExchange(X_TRANSACTIONS, true, true),
							   new FanoutExchange(X_OFFLINE_REPORTS, true, true),
							   
							   new Queue(Q_TRANSACTIONS,  true, false, true, null),
							   new Binding(Q_TRANSACTIONS, DestinationType.QUEUE, X_TRANSACTIONS, "", null),
							   
							   new Queue(Q_OFFLINE_REPORTS,  true, false, true, null),
							   new Binding(Q_OFFLINE_REPORTS, DestinationType.QUEUE, X_OFFLINE_REPORTS, "", null)

				);
	}
}