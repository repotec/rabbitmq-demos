package com.java.rabbitmq.core.callbacks;

import java.io.IOException;

@FunctionalInterface
public interface SubscriberValidationCallback<M> {
	boolean check(M message) throws IOException;
}
