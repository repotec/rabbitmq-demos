package com.demo.config;

import java.util.Map;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.retry.policy.SimpleRetryPolicy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitConfig {
	@Bean
	public ObjectMapper objectMappger() {
		return JsonMapper.builder().findAndAddModules().build();
	}

	@Autowired
	ConnectionFactory connectionFactory;

	@Bean
	public RabbitRetryTemplateCustomizer rabbitRetryTemplateCustomizer(
			
		@Value("${spring.rabbitmq.listener.simple.retry.max-attempts}") int maxAttempts) {
		
		Map<Class<? extends Throwable>, Boolean> exceptionMap 
							= Map.of(MessageDeliveryException.class, false);
		
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempts, exceptionMap, true, true);
		return (target, retryTemplate) -> retryTemplate.setRetryPolicy(retryPolicy);
	}
	
//	@Bean
//	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
//	    
//	    final var rabbitTemplate = new RabbitTemplate(connectionFactory);
//	    RetryTemplate retryTemplate = new RetryTemplate();
//	    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
//	    backOffPolicy.setInitialInterval(500);
//	    backOffPolicy.setMultiplier(3.0);
//	    backOffPolicy.setMaxInterval(3000_000);
//	    retryTemplate.setBackOffPolicy(backOffPolicy);
//	    SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
//	    simpleRetryPolicy.setMaxAttempts(15);
//	    
//	    retryTemplate.setRetryPolicy(simpleRetryPolicy);
//	    rabbitTemplate.setRetryTemplate(retryTemplate);
//
//	    return rabbitTemplate;
//	}
}
