package com.demo.consumer;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MarketingConsumer {

	public static final Logger log = LoggerFactory.getLogger(MarketingConsumer.class);

	@Autowired
	private ObjectMapper objectMapper;

	@RabbitListener(queues = "q.marketing.work")
	public void listener(String message) throws IOException {
		var employee = objectMapper.readValue(message, Employee.class);
		
		// process the image
		if (StringUtils.isEmpty(employee.getName())) {
			// throw exception, we will use DLX handler for retry mechanism
			log.info("consume marketing employee:{}", employee);
			throw new IllegalArgumentException("Employee name is null");
		}
		
		log.info("consume marketing employee:{}", employee);
	}
}
