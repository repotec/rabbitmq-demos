package com.spring.rebbitmq.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OneQueueAgainstMultipleMessageTypesConsumerApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(OneQueueAgainstMultipleMessageTypesConsumerApplication.class, args);
	}
}
