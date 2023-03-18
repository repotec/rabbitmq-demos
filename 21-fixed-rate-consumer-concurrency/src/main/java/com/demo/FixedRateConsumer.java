package com.demo;


import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class FixedRateConsumer {

	private static final Logger logger = LoggerFactory.getLogger(FixedRateConsumer.class);

	// every consumer will subscribe own message within 1 to 2 seconds
	// we create 3 concurrency consumers and maximum 7
	@RabbitListener(queues = "q.fixed-rate", concurrency = "3-7")
	public void consumerMessage(String message) {
		try {
			//business logic
			TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("consuming {} : {}", message, Thread.currentThread().getName());
	}
}
