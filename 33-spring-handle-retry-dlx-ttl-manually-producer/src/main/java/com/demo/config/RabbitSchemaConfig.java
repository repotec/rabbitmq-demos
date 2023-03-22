package com.demo.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Author: Ahmed AbdelMaksoud
 * we do not need wait queues, as we are going to put some configuration in spring property file to take care of waiting mechanism
 * unlike handling the retry mechanism manually, X_DEAD is going to be as dead-letter-exchange for X_WORK queues.
 *
 */
@Configuration
public class RabbitSchemaConfig {
	public static final Logger log = LoggerFactory.getLogger(RabbitSchemaConfig.class);
	
	private final String X_WORK = "x.work";
	private final String X_DEAD = "x.dead";
	
	//Queue for main logic (work phase)
	private final String Q_IMAGE_WORK = "q.image.work";
	private final String Q_VECTOR_WORK = "q.vector.work";
	
	
	//Queues for permanent rejection (dead phase)
	private final String Q_IMAGE_DEAD = "q.image.dead";
	private final String Q_VECTOR_DEAD = "q.vector.dead";
	
	@Bean
	public Declarables rabbitSchema() {
		
		log.info("start creating exchanges, queue, and bindings....");
		
		return new Declarables(new DirectExchange(X_WORK),
							   new DirectExchange(X_DEAD),
							   
							   new Queue(Q_IMAGE_WORK,  true, false, false, Map.of("x-dead-letter-exchange", X_DEAD)),
							   new Queue(Q_VECTOR_WORK, true, false, false, Map.of("x-dead-letter-exchange", X_DEAD)),
							   
							   new Queue(Q_IMAGE_DEAD, true, false, false, null),
							   new Queue(Q_VECTOR_DEAD, true, false, false, null),
							   
							   new Binding(Q_IMAGE_WORK, DestinationType.QUEUE, X_WORK, "jpg", null),
							   new Binding(Q_IMAGE_WORK, DestinationType.QUEUE, X_WORK, "png", null),
							   new Binding(Q_VECTOR_WORK, DestinationType.QUEUE, X_WORK, "svg", null),
							   
							   new Binding(Q_IMAGE_DEAD, DestinationType.QUEUE, X_DEAD, "jpg", null),
							   new Binding(Q_IMAGE_DEAD, DestinationType.QUEUE, X_DEAD, "png", null),
							   new Binding(Q_VECTOR_DEAD, DestinationType.QUEUE, X_DEAD, "svg", null)
				);
	}
}