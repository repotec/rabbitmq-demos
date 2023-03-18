package com.demo.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.Picture;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Consumer {
	private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

	@Autowired
	ObjectMapper mapper;
	
	@RabbitListener(queues = {"q.picture.image", "q.picture.vector", "q.picture.log", "q.picture.filter"})
	public void listener(String message) throws IOException{
		var json = mapper.readValue(message, Picture.class);
		logger.info("picture {}", json);
	}
}
