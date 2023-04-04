package com.demo.consumer.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 
 * @author ahmed
 *
 * This configuration only affects to messages which sent through RabbitTemplate object
 */

@Configuration
public class MessageConverterConfig {

	@Bean
	public ObjectMapper objectMappger() {
		return JsonMapper.builder().findAndAddModules().build();
	}
	
	//take care of deserializing the JSON messages to Java classes
	@Bean
	public Jackson2JsonMessageConverter converter (@Autowired final ObjectMapper objectMappger) {
		return new Jackson2JsonMessageConverter(objectMappger);
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate (@Autowired final ConnectionFactory connectionFactory, 
										  @Autowired final Jackson2JsonMessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		return rabbitTemplate;
	}
}
