package com.spring.rabbitmq.demo.producer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.rabbitmq.demo.producer.model.OfflineReport;
import com.spring.rabbitmq.demo.producer.model.Transaction;

@Service
public class Producer {

	public static final Logger log = LoggerFactory.getLogger(Producer.class);
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	public void sendTransactionMessage(Transaction transaction) throws IOException{
		rabbitTemplate.convertAndSend("x.transactions", "", transaction);
	}
	
	public void sendOfflineReportMessage(OfflineReport offlineReport) throws IOException {
		rabbitTemplate.convertAndSend("x.offline-reports", "", offlineReport);
	}
}