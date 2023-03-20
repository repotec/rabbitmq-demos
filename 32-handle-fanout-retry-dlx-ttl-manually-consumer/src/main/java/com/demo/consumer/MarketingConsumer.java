package com.demo.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.demo.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

@Service
public class MarketingConsumer {

	public static final Logger log = LoggerFactory.getLogger(MarketingConsumer.class);

	@Autowired
	private ObjectMapper objectMapper;

	@RabbitListener(queues = "q.marketing.work")
	public void listen(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
		
		var employee = objectMapper.readValue(message.getBody(), Employee.class);
		
		log.info("consume:" + employee.toString());
		// process any employee even if the employee name is null
		// you must acknowledge that message already processed
		channel.basicAck(deliveryTag, false);
	}
}
