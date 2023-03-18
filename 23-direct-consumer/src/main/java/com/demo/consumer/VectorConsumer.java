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
public class VectorConsumer {
	private static final Logger logger = LoggerFactory.getLogger(VectorConsumer.class);
	
	@Autowired
	ObjectMapper mapper;
	
	@RabbitListener(queues = "q.picture.vector")
	public void listener(String message) throws IOException {
		var json = mapper.readValue(message, Picture.class);
		logger.info("VectorConsumer consume picture {}", json);
	}
}
