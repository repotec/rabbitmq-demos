package com.demo.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.demo.consumer.model.Employee;

@Service
public class EmployeeConsumers {

	public static final Logger log = LoggerFactory.getLogger(EmployeeConsumers.class);

	@RabbitListener(queues = "q.work")
	public void listenImageWorkQueue(Employee employee) throws  IOException {
		log.info("Consuming image {}", employee.getName());
	}
}
