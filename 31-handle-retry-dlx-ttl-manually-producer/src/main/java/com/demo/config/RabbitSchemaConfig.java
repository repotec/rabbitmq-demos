package com.demo.config;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * we do not need wait queue here
 *
 */
@Configuration
public class RabbitSchemaConfig {
	
	private final String X_WORK = "x.work";
	private final String X_WAIT = "x.wait";
	private final String X_DEAD = "x.dead";
	
	//Queue for main logic (work phase)
	private final String Q_IMAGE_WORK = "q.image.work";
	private final String Q_VECTOR_WORK = "q.vector.work";
	
	//Queues for temporary rejection (wait phase)
	private final String Q_IMAGE_WAIT = "q.image.wait";
	private final String Q_VECTOR_WAIT = "q.vector.wait";
	
	//Queues for permanent rejection (dead phase)
	private final String Q_IMAGE_DEAD = "q.image.dead";
	private final String Q_VECTOR_DEAD = "q.vector.dead";
	
	@Bean
	public Declarables rabbitSchema() {
		return new Declarables(new DirectExchange(X_WORK),
							   new DirectExchange(X_WAIT),
							   new DirectExchange(X_DEAD),
							   
							   new Queue(Q_IMAGE_WORK,  true, false, false, Map.of("x-dead-letter-exchange", X_WAIT)),
							   new Queue(Q_VECTOR_WORK, true, false, false, Map.of("x-dead-letter-exchange", X_WAIT)),
							   
							   //wait for 4 seconds before try again  (they are failed so we need to re-process them again)
							   
							   //try again means republish it back to WORK exchanges as we want to process them again
							   
							   //if messages does not be handled and moved to dead exchange within 4 seconds it will be lost
							   
							   //messages that are in wait queue (which send by TTL argument in work queue) consumer will try to re-process them, if the
							   //consumer fail to re-process them, then will return them back to work queue, it consumer fails after 3 attempts will send them to
							   //dead queue
							   
							   new Queue(Q_IMAGE_WAIT, true, false, false, Map.of("x-dead-letter-exchange", X_WORK, "x-message-ttl", 4000)),
							   new Queue(Q_VECTOR_WAIT, true, false, false, Map.of("x-dead-letter-exchange", X_WORK, "x-message-ttl", 4000)),
							   
							   //after 3 attempts - messages will be published to dead exchange to consider them by email to manager 
							   new Queue(Q_IMAGE_DEAD, true, false, false, null),
							   new Queue(Q_VECTOR_DEAD, true, false, false, null),
							   
							   //binding with routing keys for each direct exchange
							   new Binding(Q_IMAGE_WORK, DestinationType.QUEUE, X_WORK, "jpg", null),
							   new Binding(Q_IMAGE_WORK, DestinationType.QUEUE, X_WORK, "png", null),
							   new Binding(Q_VECTOR_WORK, DestinationType.QUEUE, X_WORK, "svg", null),
							   
							   new Binding(Q_IMAGE_WAIT, DestinationType.QUEUE, X_WAIT, "jpg", null),
							   new Binding(Q_IMAGE_WAIT, DestinationType.QUEUE, X_WAIT, "png", null),
							   new Binding(Q_VECTOR_WAIT, DestinationType.QUEUE, X_WAIT, "svg", null),
							   
							   new Binding(Q_IMAGE_DEAD, DestinationType.QUEUE, X_DEAD, "jpg", null),
							   new Binding(Q_IMAGE_DEAD, DestinationType.QUEUE, X_DEAD, "png", null),
							   new Binding(Q_VECTOR_DEAD, DestinationType.QUEUE, X_DEAD, "svg", null)
				);
	}
}