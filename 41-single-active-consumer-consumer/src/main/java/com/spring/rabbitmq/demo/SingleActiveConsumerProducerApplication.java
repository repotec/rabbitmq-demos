package com.spring.rabbitmq.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SingleActiveConsumerProducerApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SingleActiveConsumerProducerApplication.class, args);
	}
}
