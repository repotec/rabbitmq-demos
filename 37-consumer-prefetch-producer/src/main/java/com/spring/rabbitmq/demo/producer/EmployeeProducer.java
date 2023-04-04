package com.spring.rabbitmq.demo.producer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.rabbitmq.demo.producer.model.Employee;

@Service
public class EmployeeProducer {

	public static final Logger log = LoggerFactory.getLogger(EmployeeProducer.class);
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	public void sendMessage(Employee employee) throws IOException {
		rabbitTemplate.convertAndSend("x.work", "", employee);
	}
}