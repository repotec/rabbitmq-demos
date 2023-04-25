package com.spring.rabbitmq.demo;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spring.rabbitmq.demo.model.Employee;
import com.spring.rabbitmq.demo.producer.EmployeeProducer;

@SpringBootApplication
public class ReliablePublishProducerApplication implements CommandLineRunner {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ReliablePublishProducerApplication.class, args);
	}
	
	@Autowired
	private EmployeeProducer producer;

	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 500; i++) {
			Employee employee = new Employee();
			employee.setId(i);
			employee.setName("employee " + i);
			employee.setBirthOfdate(LocalDate.of(1986, 11, 1));
			
			producer.sendMessage(employee);
		}
	}
}
