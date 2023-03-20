package com.demo;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.demo.model.Employee;
import com.demo.producer.EmployeeProducer;

@SpringBootApplication
public class HandleFanoutRetryDlxTtlManuallyProducer implements CommandLineRunner {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(HandleFanoutRetryDlxTtlManuallyProducer.class, args);
	}
	
	@Autowired
	EmployeeProducer employeeProducer;
	
	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 10; i++) {
			Employee employee = new Employee();
			employee.setId(i);
			//employee.setName("employee " + i); send employee message without name
			employee.setBirthOfdate(LocalDate.now());
			
			employeeProducer.sendMessage(employee);
		}
	}
}
