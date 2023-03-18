package com.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.demo.model.Furniture;
import com.demo.producer.FurnitureProducer;

@SpringBootApplication
public class HeaderProducerApplication implements CommandLineRunner {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(HeaderProducerApplication.class, args);
	}
	
	@Autowired
	FurnitureProducer furnitureProducer;
		
	private final List<String> COLORS = List.of("white", "red", "blue");
	private final List<String> MATERIALS = List.of("wood", "steel", "plastik");
	
	@Override
	public void run(String... args) throws Exception {
		for (int i = 1; i <= 10; i++) {
			var furniture = new Furniture();
			furniture.setName("furniture-00" + i);
			furniture.setColor(COLORS.get(i % COLORS.size()));
			furniture.setMaterial(MATERIALS.get(i % MATERIALS.size()));
			furniture.setPrice(i * 1000);
			
			furnitureProducer.sendMessage(furniture);
		}
	}
}
