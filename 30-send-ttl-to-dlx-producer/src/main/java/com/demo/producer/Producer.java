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
public class Producer {

	public static final Logger log = LoggerFactory.getLogger(Producer.class);
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	public void sendMessage(Picture picture) throws IOException {
		var json = mapper.writeValueAsString(picture);
		rabbitTemplate.convertAndSend("x.pictures", picture.getSource(), json);
	}
}
