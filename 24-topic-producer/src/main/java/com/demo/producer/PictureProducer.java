package com.demo.producer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.Picture;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PictureProducer {
	private static final Logger logger = LoggerFactory.getLogger(PictureProducer.class);
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	public void sendMessage(Picture picture) throws IOException {
		var json = mapper.writeValueAsString(picture);
		
		var sb = new StringBuilder();
		sb.append(picture.getSource());
		
		sb.append(".");
		
		if(picture.getSize() > 4000) {
			sb.append("large");
		}else {
			sb.append("small");
		}
		
		sb.append(".");
		
		sb.append(picture.getType());
		
		String routingKey = sb.toString();
		logger.info(picture.getName() + "|routingKey:" + routingKey);
		
		rabbitTemplate.convertAndSend("x.picture.topic", routingKey, json);
	}
}
