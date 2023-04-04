package com.spring.rabbitmq.demo;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spring.rabbitmq.demo.producer.Producer;
import com.spring.rabbitmq.demo.producer.model.OfflineReport;
import com.spring.rabbitmq.demo.producer.model.Transaction;

@SpringBootApplication
public class MutilpleConsumerPrefetchProducerApplication implements CommandLineRunner {
	public static final Logger log = LoggerFactory.getLogger(MutilpleConsumerPrefetchProducerApplication.class);

	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MutilpleConsumerPrefetchProducerApplication.class, args);
	}
	
	@Autowired
	Producer producer;
	
	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 2000; i++) {
			Transaction trans = new Transaction();
			trans.setTransaction_id(i);
			trans.setTransaction_date(LocalDate.now());
			trans.setQuantity(i);
			trans.setTaxAmount(i * 0.100);
			
			producer.sendTransactionMessage(trans);
		}
		
		for (int i = 0; i < 200; i++) {
			OfflineReport report = new OfflineReport();
			report.setNumOfTransaction(i * 100);
			report.setTotalAmount(Double.valueOf(i * 1000));
			report.setDateFrom(LocalDate.now());
			report.setDateFrom(LocalDate.now());
			
			producer.sendOfflineReportMessage(report);
		}
	}
}
