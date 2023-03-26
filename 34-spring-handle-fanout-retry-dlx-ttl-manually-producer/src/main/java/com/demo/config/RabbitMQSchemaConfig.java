package com.demo.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 
 *  @author ahmed
 * 
 * Exchange Schema configuration in case of the apply retry on exchange in fan-out as a main exhcnage
 * 
 * We need to handle routing key, so when we have two queues bind to fan-out exchange, and has one that error, 
 * only message in that particular queue go to retry mechanism. This is the schema that we will use for sample
 * retry mechanism with spring configuration.
 * 
 * To route the message to correct dead queue, we need routing key
 * We will set the routing key when we define work queue, using RabbitMQ header.
 * No programming required, we will use rabbitMQ feature for this.
 * 
 * 1- The Producer will start queue some messages
 * 2- The Consumer based on certain business rules will reject some messages (raise an exception)
 * 3- Based on Spring-RabbitMQ configuration in the consumer will retry 5 times with 3 seconds interval, multiplier is 2 and max-interval will 10 seconds
 *    initial-interval: 3s
 *    max-interval: 10s
 *    max-attempts: 5
 *    multiplier: 2
 * 4- after fifth time, if the consumer cannot handle the reject messages, then will send them to dead queue through dead exchange with consideration 
 * 	  of the routing key
 *    
 *    with spring we don't need to wait exchanges, retry exchange, and wait queues.
 *
 */
@Configuration
public class RabbitMQSchemaConfig {
	public static final Logger log = LoggerFactory.getLogger(RabbitMQSchemaConfig.class);

	private final String X_WORK = "x.work";
	private final String X_DEAD = "x.dead";
	
	//Queue for main logic (work phase)
	private final String Q_MARKETING_WORK = "q.marketing.work";
	private final String Q_ACCOUNTING_WORK = "q.accounting.work";
	
	//Queues for permanent rejection (dead phase)
	private final String Q_MARKETING_DEAD = "q.marketing.dead";
	private final String Q_ACCOUNTING_DEAD = "q.accounting.dead";

	private final String MARKETING_KEY = "dead.marketing";
	private final String ACCOUNTING_KEY = "dead.accounting";

	@Bean
	public Declarables rabbitSchema() {
		log.info("start configure RabbitMQ schema using delcarables...");
		
		return new Declarables(new FanoutExchange(X_WORK, true, false),
							   new DirectExchange(X_DEAD, true, false),
							   
							   //work queue
							   new Queue(Q_MARKETING_WORK,  true, false, false, Map.of("x-dead-letter-exchange", X_DEAD, 
									   												   "x-dead-letter-routing-key", MARKETING_KEY)),
							   new Queue(Q_ACCOUNTING_WORK, true, false, false, Map.of("x-dead-letter-exchange", X_DEAD, 
																					   "x-dead-letter-routing-key", ACCOUNTING_KEY)),
							   
							   new Queue(Q_MARKETING_DEAD,  true, false, false, null),
							   new Queue(Q_ACCOUNTING_DEAD, true, false, false, null),

							   //work fan-out exchange bindings with work queues
							   new Binding(Q_MARKETING_WORK, DestinationType.QUEUE, X_WORK, "", null),
							   new Binding(Q_ACCOUNTING_WORK, DestinationType.QUEUE, X_WORK, "", null),
							   
							   //dead exchange key bindings with dead queues
							   new Binding(Q_MARKETING_DEAD, DestinationType.QUEUE, X_DEAD, MARKETING_KEY, null),
							   new Binding(Q_ACCOUNTING_DEAD, DestinationType.QUEUE, X_DEAD, ACCOUNTING_KEY, null)
				);
	}
}