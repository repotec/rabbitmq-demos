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
public class DirectProducerApplication implements CommandLineRunner {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(DirectProducerApplication.class, args);
	}
	
	@Autowired
	Producer pictureProducer;
		
	private final List<String> types = List.of("jpg", "png", "svg");
	
	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 10; i++) {
			Picture picture = new Picture();
			picture.setName("picture " + i);
			picture.setType(types.get(i % types.size()));
			picture.setSource("mobile");
			picture.setSize(ThreadLocalRandom.current().nextLong(1,10000));
			
			pictureProducer.sendMessage(picture);
		}
	}
}
