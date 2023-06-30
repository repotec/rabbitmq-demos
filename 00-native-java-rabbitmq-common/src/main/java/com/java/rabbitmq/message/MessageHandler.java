package com.java.rabbitmq.message;


@FunctionalInterface
public interface MessageHandler {
	public void handleMessage(String message);

}
