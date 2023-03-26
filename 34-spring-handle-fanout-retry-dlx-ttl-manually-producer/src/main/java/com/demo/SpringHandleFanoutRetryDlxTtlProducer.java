package com.demo;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.demo.model.Employee;
import com.demo.producer.EmployeeProducer;

@SpringBootApplication
public class SpringHandleFanoutRetryDlxTtlProducer implements CommandLineRunner {
	public static final Logger log = LoggerFactory.getLogger(SpringHandleFanoutRetryDlxTtlProducer.class);

	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringHandleFanoutRetryDlxTtlProducer.class, args);
	}
	
	@Autowired
	EmployeeProducer employeeProducer;
	
	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 1; i++) {
			Employee employee = new Employee();
			employee.setId(i);
			//employee.setName("employee " + i); send employee message without name
			employee.setBirthOfdate(LocalDate.of(1986, 11, i + 1));
			
			employeeProducer.sendMessage(employee);
		}
	}
}
