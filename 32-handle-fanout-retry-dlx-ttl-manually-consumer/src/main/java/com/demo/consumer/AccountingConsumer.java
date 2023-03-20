package com.demo.consumer;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.demo.model.Employee;
import com.demo.retry.DlxProcessingErrorHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

@Service
public class AccountingConsumer {

	public static final Logger log = LoggerFactory.getLogger(AccountingConsumer.class);
	
	private static final String DEAD_EXCHANGE_NAME = "x.dead";

	@Autowired
	private ObjectMapper objectMapper;

	@RabbitListener(queues = "q.accounting.work")
	public void listen(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag)
			throws InterruptedException, JsonParseException, JsonMappingException, IOException {
		try {
			var employee = objectMapper.readValue(message.getBody(), Employee.class);
			
			// process the image
			if (StringUtils.isEmpty(employee.getName())) {
				// throw exception, we will use DLX handler for retry mechanism
				log.info("could not consume employee:{}", employee);
				throw new IllegalArgumentException("Employee name is null");
			} else {
				log.info("consumering employee info:{}", employee);
				
				// you must acknowledge that message already processed
				channel.basicAck(deliveryTag, false);
			}
		} catch (IllegalArgumentException e) {
			log.warn("Error processing message : " + new String(message.getBody()) + " : " + e.getMessage());
			
			//1. prepare DlxProcessingErrorHandler object with DLX exchange name passed to the constructor 
			DlxProcessingErrorHandler handler = new DlxProcessingErrorHandler(DEAD_EXCHANGE_NAME);
			
			//2. call handleErrorProcessingMessage method to handle the rejection manually
			handler.handleErrorProcessingMessage(message, channel, deliveryTag);
		}
	}
}
