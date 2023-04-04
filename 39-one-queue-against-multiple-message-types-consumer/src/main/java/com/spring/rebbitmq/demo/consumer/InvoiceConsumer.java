package com.spring.rebbitmq.demo.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.spring.rebbitmq.demo.model.InvoiceCreatedMessage;
import com.spring.rebbitmq.demo.model.InvoicePaidMessage;

@Service
@RabbitListener(queues = "q.invoice")
public class InvoiceConsumer {

	private static final Logger logger = LoggerFactory.getLogger(InvoiceConsumer.class);
	
	@RabbitHandler
	public void listenInvoiceCreated(InvoiceCreatedMessage message) {
		logger.info("Invoice Created {}" + message);
	}
	
	@RabbitHandler
	public void listenInvoicePaid(InvoicePaidMessage message) {
		logger.info("Invoice Paid {}" + message);
	}
	
	@RabbitHandler(isDefault = true)
	public void defaultListener(Object message) {
		logger.info("{}" + message);
	}
}
