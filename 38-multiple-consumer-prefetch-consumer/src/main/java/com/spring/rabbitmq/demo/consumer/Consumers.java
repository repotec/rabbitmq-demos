package com.spring.rabbitmq.demo.consumer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.spring.rabbitmq.demo.consumer.model.OfflineReport;
import com.spring.rabbitmq.demo.consumer.model.Transaction;

@Service
public class Consumers {
	public static final Logger log = LoggerFactory.getLogger(Consumers.class);

	@RabbitListener(queues = "q.transactions", concurrency = "2")
	public void transactionQueueListener(Transaction transaction) throws  IOException, InterruptedException {
		log.info("Consuming transaction {}", transaction.toString());
		TimeUnit.MILLISECONDS.sleep(100);
	}
	
	@RabbitListener(queues = "q.offline-reports", concurrency = "2", containerFactory = "prefetchOneContainerFactory")
	public void offlineReportQueueListener(OfflineReport offlineReport) throws  IOException, InterruptedException {
		log.info("Consuming offline-report {}", offlineReport.toString());
		TimeUnit.MINUTES.sleep(1);
	}
}