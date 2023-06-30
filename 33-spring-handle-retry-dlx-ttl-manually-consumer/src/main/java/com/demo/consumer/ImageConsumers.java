package com.demo.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.stereotype.Service;

import com.demo.model.Picture;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ImageConsumers {

	public static final Logger log = LoggerFactory.getLogger(ImageConsumers.class);

	@Autowired
	private ObjectMapper objectMapper;

	@RabbitListener(queues = "q.image.work")
	public void listenImageWorkQueue(String message) throws  IOException {
		MessageConsumer(message, true);
	}
	
	@RabbitListener(queues = {"q.image.work"})
	public void listenVectorWorkQueue(String message) throws  IOException {
		MessageConsumer(message, false);
	}
	
	private void MessageConsumer(String message, boolean validate) throws IOException, StreamReadException, DatabindException {
		var picture = objectMapper.readValue(message, Picture.class);
		
		log.info("Consuming image {}", picture.getName());
		
		if(validate) {
			if (picture.getSize() > 9000) {
				// throw exception, we will use DLX handler for retry mechanism
				throw new IllegalArgumentException("Size too large");
			} else if (picture.getType().equals("jpg")) {
				// throw exception, we will be ignored from retry mechanism - will be send directly to DLX
				throw new MessageDeliveryException("wrong image type");
			}
		}
		
		log.info("Processing image : " + picture.getName());
	}
}
