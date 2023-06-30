package com.demo.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.demo.model.Picture;
import com.demo.retry.DlxProcessingErrorHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

@Service
public class ImageConsumer {

	public static final Logger log = LoggerFactory.getLogger(ImageConsumer.class);
	
	private static final String DEAD_EXCHANGE_NAME = "x.dead";

	@Autowired
	private ObjectMapper objectMapper;

	@RabbitListener(queues = "q.image.work")
	public void listen(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag)
			throws InterruptedException, JsonParseException, JsonMappingException, IOException {
		try {
			var picture = objectMapper.readValue(message.getBody(), Picture.class);
			
			// process the image
			if (picture.getSize() > 9000) {
				// throw exception, we will use DLX handler for retry mechanism
				throw new IOException("Size too large");
				
			} else {
				log.info("Creating thumbnail & publishing : " + picture);
				
				// you must acknowledge that message already processed - multiple = false
				channel.basicAck(deliveryTag, false);
			}
		} catch (IOException e) {
			log.warn("Error processing message : " + new String(message.getBody()) + " : " + e.getMessage());
			
			//1. prepare DlxProcessingErrorHandler object with DLX exchange name passed to the constructor 
			DlxProcessingErrorHandler handler = new DlxProcessingErrorHandler(DEAD_EXCHANGE_NAME);
			
			//2. call handleErrorProcessingMessage method to handle the rejection manually
			handler.handleErrorProcessingMessage(message, channel, deliveryTag);
		}
	}
}
