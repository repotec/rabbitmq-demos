package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.demo.model.Picture;
import com.demo.producer.PictureProducer;

@SpringBootApplication
public class SpringHandleRetryDlxTtlManuallyProducer implements CommandLineRunner {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringHandleRetryDlxTtlManuallyProducer.class, args);
	}
	
	@Autowired
	PictureProducer pictureProducer;
		
	@Override
	public void run(String... args) throws Exception {
		Picture picture1 = new Picture();
		picture1.setName("picture " + 1);
		picture1.setType("jpg");
		picture1.setSource("web");
		picture1.setSize(800);
		
		pictureProducer.sendMessage(picture1);
		
		Picture picture2 = new Picture();
		picture2.setName("picture " + 2);
		picture2.setType("png");
		picture2.setSource("web");
		picture2.setSize(1000);
		
		pictureProducer.sendMessage(picture2);
	}
}
