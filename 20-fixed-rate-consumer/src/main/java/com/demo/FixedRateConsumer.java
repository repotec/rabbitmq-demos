package com.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class FixedRateConsumer {

	private static final Logger logger = LoggerFactory.getLogger(FixedRateConsumer.class);

	@RabbitListener(queues = "q.fixed-rate")
	public void consumerMessage(String message) {
		logger.info("consuming {}", message);
	}
}
