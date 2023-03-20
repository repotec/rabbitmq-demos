package com.demo.config;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitSchemaConfig {
	
	private final String X_WORK = "x.work";
	private final String X_WAIT = "x.wait";
	private final String X_DEAD = "x.dead";
	private final String X_RETRY = "x.retry";
	
	//Queue for main logic (work phase)
	private final String Q_MARKETING_WORK = "q.marketing.work";
	private final String Q_ACCOUNTING_WORK = "q.accounting.work";
	
	//Queues for temporary rejection (wait phase)
	private final String Q_MARKETING_WAIT = "q.marketing.wait";
	private final String Q_ACCOUNTING_WAIT = "q.accounting.wait";
	
	//Queues for permanent rejection (dead phase)
	private final String Q_MARKETING_DEAD = "q.marketing.dead";
	private final String Q_ACCOUNTING_DEAD = "q.accounting.dead";

	private final String MARKETING_KEY = "marketing";
	private final String ACCOUNTING_KEY = "accounting";
	/**
	 * Design
	 * ******
	 * Demonstrate how can we apply retry mechanism for fan-out exchanges
	 * 
	 * We have 4 exchanges
	 * *******************
	 * fan-out x.work
	 * direct x.wait  -> Wait is the pair which invalid messages will be wait for some time before sent to retry exchange
	 * 					 it uses time to live (TTL) and dead letter exchange (DLX) to achieve this.
	 *
	 * directx.dead	  -> Dead is the pair which invalid messages has been retried for N times, but still the message is invalid and has error.
	 * direct x.retry -> Retry is additional exchange for routing invalid message to correct queue, using routing key.
	 * 
	 * We have six queues:
	 * *******************
	 * q.marketing.work
	 * q.accounting.work
	 * 
	 * q.marketing.wait
	 * q.accounting.wait
	 * 
	 * q.marketing.dead
	 * q.accounting.dead
	 * 	
	 *
	 * publisher will publish a message to work fan-out queue
	 *
	 * We can consider retry exchange as a filter proxy exchange 
	 * This is because work exchange is fan-out. That means if we push invalid message from wait to work, the message will be broadcasted again
	 * That means if you have two queue bindings on work : “accounting” and “marketing”, nd only “accounting” that errors, when retry mechanism 
	 * push message from wait directly to work, “marketing” will get another same messages, so it will be duplicate process on “marketing”
	 *
	 */
	@Bean
	public Declarables rabbitSchema() {
		return new Declarables(new FanoutExchange(X_WORK),
							   new DirectExchange(X_WAIT),
							   new DirectExchange(X_DEAD),
							   new DirectExchange(X_RETRY),
							   
							   ////////////////////////////////////////////////////////////////////////////////////////////////////
							   
							   //work queue
							   new Queue(Q_MARKETING_WORK,  true, false, false, Map.of("x-dead-letter-exchange", X_WAIT, 
									   												   "x-dead-letter-routing-key", MARKETING_KEY)),
							   new Queue(Q_ACCOUNTING_WORK, true, false, false, Map.of("x-dead-letter-exchange", X_WAIT, 
																					   "x-dead-letter-routing-key", ACCOUNTING_KEY)),
							   //wait queue
							   new Queue(Q_MARKETING_WAIT, true, false, false, Map.of("x-dead-letter-exchange", X_RETRY, 
									   												   "x-dead-letter-routing-key", MARKETING_KEY,
									   												   "x-message-ttl", 10000)),
							   new Queue(Q_ACCOUNTING_WAIT, true, false, false, Map.of("x-dead-letter-exchange", X_RETRY, 
																					   "x-dead-letter-routing-key", ACCOUNTING_KEY,
																					   "x-message-ttl", 10000)),
							   
							   //dead queue
							   new Queue(Q_MARKETING_DEAD, true, false, false, Map.of("x-query-mode", "lazy")),
							   new Queue(Q_ACCOUNTING_DEAD, true, false, false, Map.of("x-query-mode", "lazy")),
							   
							   ////////////////////////////////////////////////////////////////////////////////////////////////////
							   
							   //work exchange key bindings with work queues
							   new Binding(Q_MARKETING_WORK, DestinationType.QUEUE, X_WORK, MARKETING_KEY, null),
							   new Binding(Q_ACCOUNTING_WORK, DestinationType.QUEUE, X_WORK, ACCOUNTING_KEY, null),

							   //wait exchange key bindings with wait queues
							   new Binding(Q_MARKETING_WAIT, DestinationType.QUEUE, X_WAIT, MARKETING_KEY, null),
							   new Binding(Q_ACCOUNTING_WAIT, DestinationType.QUEUE, X_WAIT, ACCOUNTING_KEY, null),
							   
							   //retry exchange key bindings with work queues
							   new Binding(Q_MARKETING_WORK, DestinationType.QUEUE, X_RETRY, MARKETING_KEY, null),
							   new Binding(Q_ACCOUNTING_WORK, DestinationType.QUEUE, X_RETRY, ACCOUNTING_KEY, null),
							   
							   //dead exchange key bindings with dead queues
							   new Binding(Q_MARKETING_DEAD, DestinationType.QUEUE, X_DEAD, MARKETING_KEY, null),
							   new Binding(Q_ACCOUNTING_DEAD, DestinationType.QUEUE, X_DEAD, ACCOUNTING_KEY, null)
				);
	}
}