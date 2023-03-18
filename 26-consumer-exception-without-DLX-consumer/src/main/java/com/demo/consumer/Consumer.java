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
	
	@RabbitListener(queues = "q.consumer-exception-without-dlx")
	public void listener(String message) throws IOException{
		var picture = mapper.readValue(message, Picture.class);
		
		//By default, all failed messages will be immediately re-queued at the head of the target queue over and over again.
		if(picture.getSize() > 9000) {
			throw new IllegalArgumentException("error: picture size is too large!");
		}
		logger.info("picture {}", picture);
	}
}
