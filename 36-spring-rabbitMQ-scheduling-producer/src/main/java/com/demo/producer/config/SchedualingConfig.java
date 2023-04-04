package com.demo.producer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedualingConfig {
	
	public static final Logger log = LoggerFactory.getLogger(SchedualingConfig.class);
	
	@Autowired
	private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
	
	@Scheduled(cron = "0 50 0 * * *")
	public void stopAll() {
		rabbitListenerEndpointRegistry.getListenerContainers().forEach(container -> {
			log.info("stop listener containers...");
			container.stop();
		});
	}
	
	@Scheduled(cron = "0 5 1 * * *")
	public void startAll() {
		rabbitListenerEndpointRegistry.getListenerContainers().forEach(container -> {
			log.info("start listener containers...");
			container.start();
		});
	}
}
