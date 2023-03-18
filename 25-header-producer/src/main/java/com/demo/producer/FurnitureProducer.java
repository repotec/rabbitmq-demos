package com.demo.producer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.Furniture;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FurnitureProducer {
	private static final Logger logger = LoggerFactory.getLogger(FurnitureProducer.class);
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	public void sendMessage(Furniture furniture) throws IOException {
		//java 7
		//MessageProperties pros = new MessageProperties();
		//pros.setHeader("color", furniture.getColor());
		//pros.setHeader("material", furniture.getMaterial());
		//
		//String json = mapper.writeValueAsString(furniture);
		//Message msg = new Message(json.getBytes(), pros);
		//
		//rabbitTemplate.send("x.promotion.header", msg);
		//logger.info("producer a new message | furniture {}", furniture);

		//java 8
		var json = mapper.writeValueAsString(furniture);
		rabbitTemplate.convertAndSend("x.promotion.header", "", json, message -> {
		     message.getMessageProperties().getHeaders().put("color" , furniture.getColor());
		     message.getMessageProperties().getHeaders().put("material" , furniture.getMaterial());
		     logger.info("producer a new message | furniture {}", furniture);
		     return message;
	    });
	}
}
