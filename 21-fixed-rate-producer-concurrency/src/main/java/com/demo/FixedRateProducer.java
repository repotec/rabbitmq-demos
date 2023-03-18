package com.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FixedRateProducer {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(FixedRateProducer.class);
	
	private int i;
	
	@Scheduled(fixedRate = 500)
	public void sendMessage() {
		i++;
		logger.info("i is:{}", i);
		rabbitTemplate.convertAndSend("x.fixed-rate", "", "fixed rate message:" + i);
	}
}
