package com.spring.rabbitmq.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MutipleConsumerPrefetchConsumerApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MutipleConsumerPrefetchConsumerApplication.class, args);
	}
}
