package com.demo.retry;

import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.lang.NonNull;

import com.demo.retry.model.RabbitmqHeaderModel;
import com.rabbitmq.client.Channel;

public class DlxProcessingErrorHandler {

	private static final Logger LOG = LoggerFactory.getLogger(DlxProcessingErrorHandler.class);

	@NonNull
	private String deadExchangeName;

	private int maxRetryCount = 3;

	public DlxProcessingErrorHandler(String deadExchangeName) throws IllegalArgumentException {
		super();

		if (StringUtils.isAnyEmpty(deadExchangeName)) {
			throw new IllegalArgumentException("Must define dlx exchange name");
		}

		this.deadExchangeName = deadExchangeName;
	}

	public DlxProcessingErrorHandler(String deadExchangeName, int maxRetryCount) {
		this(deadExchangeName);
		setMaxRetryCount(maxRetryCount);
	}

	public String getDeadExchangeName() {
		return deadExchangeName;
	}

	public int getMaxRetryCount() {
		return maxRetryCount;
	}

	/**
	 * this handler does two kind of processing
	 * first one: if the message doesn't exceed the number of tries, then use basicReject to send them to wait queue (DLX)
	 * second one: if the message reaches the number of tries, then use basicPublish to send them to dead queue (DLX)
	 */
	public boolean handleErrorProcessingMessage(Message message, Channel channel, long deliveryTag) {
		//convert RabbitMQ object to custom POJOs
		var rabbitMqHeader = new RabbitmqHeaderModel(message.getMessageProperties().getHeaders());

		//1. first return back the messages to work queues manually
		try {
			if (rabbitMqHeader.getFailedRetryCount() >= maxRetryCount) {
				
				LOG.warn("[DEAD] Error at " + LocalDateTime.now() + " on retry " + rabbitMqHeader.getFailedRetryCount() + " for message " + new String(message.getBody()));
				LOG.warn("[DEAD] will bw send through " + getDeadExchangeName() + " with routing key message " + message.getMessageProperties().getReceivedRoutingKey());

				// move it to work queue and considering the received RoutingKey while publishing
				channel.basicPublish(getDeadExchangeName(), message.getMessageProperties().getReceivedRoutingKey(), null, message.getBody());
				channel.basicAck(deliveryTag, false);
			} else {
				LOG.warn("[REQUEUE] Error at " + LocalDateTime.now() + " on retry " + rabbitMqHeader.getFailedRetryCount() + " for message " + new String(message.getBody()));

				// reject it - send the message to wait queue, which will wait to 3 second the then get back to work queue again, to let the consumer catch and consume it
				channel.basicReject(deliveryTag, false);
			}
			return true;
		} catch (IOException e) {
			LOG.warn("[HANDLER-FAILED] Error at " + LocalDateTime.now() + " on retry "+ rabbitMqHeader.getFailedRetryCount() + " for message " + new String(message.getBody()));
		}

		return false;
	}

	public void setMaxRetryCount(int maxRetryCount) throws IllegalArgumentException {
		if (maxRetryCount > 1000) {
			throw new IllegalArgumentException("max retry must between 0-1000");
		}

		this.maxRetryCount = maxRetryCount;
	}

}