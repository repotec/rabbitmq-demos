package com.demo.producer.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

/**
 * 
 *  @author ahmed
 * 
 */
@Configuration
public class RabbitTemplateConfig {

//	@Autowired
//	private Jackson2JsonMessageConverter jackson2JsonMessageConverter;
//	
//	@Autowired
//	private ConnectionFactory connectionFactory;
//	
//	@Bean
//	public RabbitTemplate rabbitTemplate() {
//	    final var rabbitTemplate = new RabbitTemplate(connectionFactory);
//	    rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
//	    return rabbitTemplate;
//	}
}
