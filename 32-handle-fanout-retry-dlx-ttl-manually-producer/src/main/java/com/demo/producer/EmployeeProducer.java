package com.demo.producer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmployeeProducer {

	public static final Logger log = LoggerFactory.getLogger(EmployeeProducer.class);
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	public void sendMessage(Employee employee) throws IOException {
		var json = mapper.writeValueAsString(employee);
		rabbitTemplate.convertAndSend("x.work", "", json);
	}
}
