package com.spring.rebbitmq.demo;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spring.rebbitmq.demo.model.InvoiceCreatedMessage;
import com.spring.rebbitmq.demo.producer.InvoiceProducer;

@SpringBootApplication
public class ConsistanceHashExchangeProducerApplication implements CommandLineRunner {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ConsistanceHashExchangeProducerApplication.class, args);
	}
	
	@Autowired
	InvoiceProducer producer;

	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 200; i++) {
			var randomInvNo = "INV-" + (i % 60);
			var icrm = new InvoiceCreatedMessage(19.2, LocalDate.now().minusDays(2), "USD", randomInvNo);
			producer.sendInvoiceCreated(icrm);
		}
	}
}
