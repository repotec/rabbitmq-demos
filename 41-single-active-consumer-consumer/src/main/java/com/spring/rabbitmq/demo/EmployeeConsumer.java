package com.spring.rabbitmq.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.rabbitmq.demo.model.Employee;

public class EmployeeConsumer {
	public static final Logger log = LoggerFactory.getLogger(EmployeeConsumer.class);
	
	@Autowired
	RabbitTemplate template;
	
	@RabbitListener(queues = "q.hr", concurrency = "5")
	public void consumeEmployee(Employee employee) {
		log.info("consume employee :{}", employee);
	}
}
