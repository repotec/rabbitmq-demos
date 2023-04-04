package com.demo.producer.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 *  @author ahmed
 * 
 */
@Configuration
public class Jackson2JsonMessageConverterMapper {
	
//	@Bean
//	public Jackson2JsonMessageConverter converter(@Autowired ObjectMapper objectMapper) {
//		return new Jackson2JsonMessageConverter(objectMapper);
//	}
}
