package com.demo.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.demo.model.Picture;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

@Service
public class Consumer {
	private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

	@Autowired
	ObjectMapper mapper;
	
	@RabbitListener(queues = "q.pictures")
	public void listener(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException{
		var picture = mapper.readValue(message, Picture.class);
		
		if(picture.getSize() > 9000) {
			channel.basicReject(tag, false);
			logger.info("message has been rejected. size is too large!");
		}else {
			logger.info("picture {}", picture);
		}
		//channel.basicAck(tag, false); //disable it only if the queue and exchange marked as auto-delete
	}
}
