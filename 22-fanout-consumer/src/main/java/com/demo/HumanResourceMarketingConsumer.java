package com.demo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HumanResourceMarketingConsumer {

	private static final Logger logger = LoggerFactory.getLogger(HumanResourceMarketingConsumer.class);
	
	@Autowired
	ObjectMapper mapper;
	
	@RabbitListener(queues = "q.hr.marketing")
	public void listener(String message) throws IOException {
		var employee = mapper.readValue(message, Employee.class);
		logger.info("employee is " + employee);
	}
}
