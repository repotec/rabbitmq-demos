package com.demo;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.demo.model.Picture;
import com.demo.producer.Producer;

@SpringBootApplication
public class SendTTLtoDLXProducerApplication implements CommandLineRunner {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SendTTLtoDLXProducerApplication.class, args);
	}
	
	@Autowired
	Producer producer;
		
	private final List<String> types = List.of("jpg", "png", "svg");

	private final List<String> source = List.of("mobile", "web");
	
	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 20; i++) {
			Picture picture = new Picture();
			picture.setName("picture " + i);
			picture.setType(types.get(i % types.size()));
			picture.setSource(source.get(i % source.size()));
			picture.setSize(ThreadLocalRandom.current().nextLong(9000,10000));
			
			producer.sendMessage(picture);
		}
	}
}
