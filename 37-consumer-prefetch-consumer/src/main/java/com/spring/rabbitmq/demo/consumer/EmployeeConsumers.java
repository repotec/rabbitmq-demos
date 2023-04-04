package com.spring.rabbitmq.demo.consumer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.spring.rabbitmq.demo.consumer.model.Employee;

@Service
public class EmployeeConsumers {

	public static final Logger log = LoggerFactory.getLogger(EmployeeConsumers.class);

	@RabbitListener(queues = "q.work", concurrency = "2")
	public void listenImageWorkQueue(Employee employee) throws  IOException, InterruptedException {
		log.info("Consuming employee {}", employee.getName());
		TimeUnit.SECONDS.sleep(20);
	}
}
