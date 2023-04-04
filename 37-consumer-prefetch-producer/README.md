# Consumer Prefetch

Application to demonstrate consumer prefetch mechanism using Spring and RabbitMQ

- Spring by default assing 250 as a prefetch value for each consumer

## Introduction
The RabbitMQ prefetch value is used to specify how many messages are being sent at the same time.

Messages in RabbitMQ are pushed from the broker to the consumers. The RabbitMQ default prefetch setting gives clients an unlimited buffer, meaning that RabbitMQ, by default, sends as many messages as it can to any consumer that appears ready to accept them. It is, therefore, possible to have more than one message "in flight" on a channel at any given moment.

## The problem
- We have one fanout exchange has binding with one queue, The producer client will push 500 messages.

- In the other side we have two consumers waiting these message to subscribe them, between each consuming process there is 20 seconds of delay (assume we have a heavy processing or network delaying)

- Each consumer will cache 250 messages until be processed, If we decide to deploy a new consumer, So all pre-fetched messages in that case are invisible to this new consumer and are listed as unacked messages in the RabbitMQ management interface although they are still not processed and cache my the two consumer.

- The problem above will keep the new consumer idle for long time and will impact the throughput.

## Solution
We need to set prefetch value to one if our consumers handle the messages slowly 

## Authors

- [@repotec](https://github.com/repotec)