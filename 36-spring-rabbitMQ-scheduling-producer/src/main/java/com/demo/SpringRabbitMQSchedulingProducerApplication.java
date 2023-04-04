package com.demo;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.demo.producer.EmployeeProducer;
import com.demo.producer.model.Employee;

@SpringBootApplication
public class SpringRabbitMQSchedulingProducerApplication implements CommandLineRunner {
	public static final Logger log = LoggerFactory.getLogger(SpringRabbitMQSchedulingProducerApplication.class);

	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringRabbitMQSchedulingProducerApplication.class, args);
	}
	
	@Autowired
	EmployeeProducer employeeProducer;
	
	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 10; i++) {
			Employee employee = new Employee();
			employee.setId(i);
			employee.setName("employee " + i);
			employee.setBirthOfdate(LocalDate.of(1986, 11, i + 1));
			
			employeeProducer.sendMessage(employee);
		}
	}
}
