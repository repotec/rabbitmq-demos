package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FixedRateProducerApplicatrion {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(FixedRateProducerApplicatrion.class, args);
	}

}
