package com.demo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HumanResourceAccountingConsumer {

	private static final Logger logger = LoggerFactory.getLogger(HumanResourceAccountingConsumer.class);
	
	@Autowired
	ObjectMapper objectMapper;
	
	
	@RabbitListener(queues = "q.hr.accounting")
	public void listener(String message) throws IOException  {
		var employee = objectMapper.readValue(message, Employee.class);
		logger.info("Employee is:" + employee);
	}
}
