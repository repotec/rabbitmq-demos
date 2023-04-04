package com.spring.rabbitmq.demo.consumer.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class prefetchListenerContainerFactoryConfig {

	@Bean
	public RabbitListenerContainerFactory<SimpleMessageListenerContainer> prefetchOneContainerFactory(
			SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {

		SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
		listenerContainerFactory.setPrefetchCount(1);
		configurer.configure(listenerContainerFactory, connectionFactory);
		return listenerContainerFactory;
	}
}
