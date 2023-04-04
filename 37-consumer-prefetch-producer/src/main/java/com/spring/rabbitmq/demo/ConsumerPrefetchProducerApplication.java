package com.spring.rabbitmq.demo;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spring.rabbitmq.demo.producer.EmployeeProducer;
import com.spring.rabbitmq.demo.producer.model.Employee;

@SpringBootApplication
public class ConsumerPrefetchProducerApplication implements CommandLineRunner {
	public static final Logger log = LoggerFactory.getLogger(ConsumerPrefetchProducerApplication.class);

	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ConsumerPrefetchProducerApplication.class, args);
	}
	
	@Autowired
	EmployeeProducer employeeProducer;
	
	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 500; i++) {
			Employee employee = new Employee();
			employee.setId(i);
			employee.setName("employee " + i);
			employee.setBirthOfdate(LocalDate.of(1986, 11, 1));
			
			employeeProducer.sendMessage(employee);
		}
	}
}
