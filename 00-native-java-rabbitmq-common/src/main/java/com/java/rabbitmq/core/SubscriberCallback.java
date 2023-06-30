package com.java.rabbitmq.core;

import java.io.IOException;

@FunctionalInterface
public interface SubscriberCallback<M> {
	boolean check(M message) throws IOException;
}
