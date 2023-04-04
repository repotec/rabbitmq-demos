package com.spring.rebbitmq.demo.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.rebbitmq.demo.model.InvoiceCancelledMessage;
import com.spring.rebbitmq.demo.model.InvoiceCreatedMessage;
import com.spring.rebbitmq.demo.model.InvoicePaidMessage;

@Service
public class InvoiceProducer {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	private static final String EXCHANGE = "x.invoice";
	
	public void sendInvoiceCreated(InvoiceCreatedMessage message) {
		rabbitTemplate.convertAndSend(EXCHANGE, "", message);
	}
	
	public void sendInvoicePaid(InvoicePaidMessage message) {
		rabbitTemplate.convertAndSend(EXCHANGE, "", message);
	}
	
	public void sendInvoiceCancelled(InvoiceCancelledMessage message) {
		rabbitTemplate.convertAndSend(EXCHANGE, "", message);
	}
}
