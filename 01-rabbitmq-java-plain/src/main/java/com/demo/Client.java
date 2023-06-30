package com.demo;

import java.util.concurrent.CountDownLatch;

public abstract class Client {
    protected static final String HOST_NAME = "localhost";
    protected static final String PORT_NUMBER = "5672";
    protected static final String USERNAME = "guest";
    protected static final String PASSWORD = "guest";
    
    protected static CountDownLatch latch = new CountDownLatch(1);
}
