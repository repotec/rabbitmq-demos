package com.spring.rebbitmq.demo;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spring.rebbitmq.demo.model.InvoiceCancelledMessage;
import com.spring.rebbitmq.demo.model.InvoiceCreatedMessage;
import com.spring.rebbitmq.demo.model.InvoicePaidMessage;
import com.spring.rebbitmq.demo.producer.InvoiceProducer;

@SpringBootApplication
public class OneQueueAgainstMultipleMessageTypesProducerApplication implements CommandLineRunner {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(OneQueueAgainstMultipleMessageTypesProducerApplication.class, args);
	}
	
	@Autowired
	InvoiceProducer producer;

	@Override
	public void run(String... args) throws Exception {
		var randomInvNo = "INV-" + ThreadLocalRandom.current().nextInt(100, 200);
		var icrm = new InvoiceCreatedMessage(19.2, LocalDate.now().minusDays(2), "USD", randomInvNo);
		producer.sendInvoiceCreated(icrm);
		
		randomInvNo = "INV-" + ThreadLocalRandom.current().nextInt(300, 400);
		var randomPayNo = "PAY-" + ThreadLocalRandom.current().nextInt(800, 1000);
		var ipdm = new InvoicePaidMessage(randomInvNo, LocalDate.now(), randomPayNo);
		producer.sendInvoicePaid(ipdm);
		
		// will not have a header in consumer project - default header will catch it as a general consumer
		randomInvNo = "INV-" + ThreadLocalRandom.current().nextInt(400, 500);
		var iclm = new InvoiceCancelledMessage(LocalDate.now(), randomInvNo, "not enough");
		producer.sendInvoiceCancelled(iclm);
	}
}
