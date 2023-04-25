package com.spring.rabbitmq.demo.producer;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.rabbitmq.demo.model.Employee;

@Service
public class EmployeeProducer {

	public static final Logger log = LoggerFactory.getLogger(EmployeeProducer.class);
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@PostConstruct
	private void registerCallback() {
		rabbitTemplate.setConfirmCallback((corrlation, ack, reason)->{
			if(corrlation == null) {
				return;
			}
			
			if(ack) {
				log.info("message with correlation {} published", corrlation.getId());
			}else {
				log.info("invalid message with correlation {} published", corrlation.getId());
			}
		});
		
		rabbitTemplate.setReturnsCallback(returned -> {
			log.info("return callback");
			
			if(returned.getReplyText() != null && returned.getReplyText().equalsIgnoreCase("NO_ROUTE")) {
				String id = returned.getMessage().getMessageProperties().getHeader("spring_returned_message_correlation").toString();
				log.warn("invalid reouting key for message {}", id);
			}
		});
	}
	
	public void sendMessage(Employee employee) throws IOException {
		rabbitTemplate.convertAndSend("x.hr", "", employee);
	}
}