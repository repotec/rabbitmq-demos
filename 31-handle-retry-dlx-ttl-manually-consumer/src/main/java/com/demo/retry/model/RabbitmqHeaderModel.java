package com.demo.retry.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;


public class RabbitmqHeaderModel {

	private static final String KEYWORD_QUEUE_WAIT = "wait";
	private List<RabbitmqHeaderXDeathModel> xDeaths = new ArrayList<>(2);
	private String xFirstDeathExchange = StringUtils.EMPTY;
	private String xFirstDeathQueue = StringUtils.EMPTY;
	private String xFirstDeathReason = StringUtils.EMPTY;

	@SuppressWarnings("unchecked")
	public RabbitmqHeaderModel(Map<String, Object> headers) {
		System.err.println("headers:" + headers);
		
		if (headers != null) {
			var xFirstDeathExchange = Optional.ofNullable(headers.get("x-first-death-exchange"));
			var xFirstDeathQueue = Optional.ofNullable(headers.get("x-first-death-queue"));
			var xFirstDeathReason = Optional.ofNullable(headers.get("x-first-death-reason"));

			xFirstDeathExchange.ifPresent(s -> this.setxFirstDeathExchange(s.toString()));
			xFirstDeathQueue.ifPresent(s -> this.setxFirstDeathQueue(s.toString()));
			xFirstDeathReason.ifPresent(s -> this.setxFirstDeathReason(s.toString()));

			var xDeathHeaders = (List<Map<String, Object>>) headers.get("x-death");

			if (xDeathHeaders != null) {
				// RabbitMQ will has xDeathHeaders instance for each try
				// if our message failed twice, RabbitMQ will provide two instance of xDeathHeaders, so we can store and count them
				System.err.println("xDeathHeaders:" + xDeathHeaders);
				for (Map<String, Object> x : xDeathHeaders) {
					RabbitmqHeaderXDeathModel hdrDeath = new RabbitmqHeaderXDeathModel();
					var reason = Optional.ofNullable(x.get("reason"));
					var count = Optional.ofNullable(x.get("count"));
					var exchange = Optional.ofNullable(x.get("exchange"));
					var queue = Optional.ofNullable(x.get("queue"));
					var routingKeys = Optional.ofNullable(x.get("routing-keys"));
					var time = Optional.ofNullable(x.get("time"));

					reason.ifPresent(s -> hdrDeath.setReason(s.toString()));
					count.ifPresent(s -> hdrDeath.setCount(Integer.parseInt(s.toString())));
					exchange.ifPresent(s -> hdrDeath.setExchange(s.toString()));
					queue.ifPresent(s -> hdrDeath.setQueue(s.toString()));
					routingKeys.ifPresent(r -> {
						var listR = (List<String>) r;
						hdrDeath.setRoutingKeys(listR);
					});
					time.ifPresent(d -> hdrDeath.setTime((Date) d));

					xDeaths.add(hdrDeath);
				}
			}
		}
	}

	// Compatible for how we created the exchange and queue (name should 'wait' word)
	public int getFailedRetryCount() {
		// get from queue "wait"
		for (var xDeath : xDeaths) {
			if (xDeath.getExchange().toLowerCase().endsWith(KEYWORD_QUEUE_WAIT) && xDeath.getQueue().toLowerCase().endsWith(KEYWORD_QUEUE_WAIT)) {
				return xDeath.getCount();
			}
		}

		return 0;
	}

	public List<RabbitmqHeaderXDeathModel> getxDeaths() {
		return xDeaths;
	}

	public String getxFirstDeathExchange() {
		return xFirstDeathExchange;
	}

	public String getxFirstDeathQueue() {
		return xFirstDeathQueue;
	}

	public String getxFirstDeathReason() {
		return xFirstDeathReason;
	}

	public void setxDeaths(List<RabbitmqHeaderXDeathModel> xDeaths) {
		this.xDeaths = xDeaths;
	}

	public void setxFirstDeathExchange(String xFirstDeathExchange) {
		this.xFirstDeathExchange = xFirstDeathExchange;
	}

	public void setxFirstDeathQueue(String xFirstDeathQueue) {
		this.xFirstDeathQueue = xFirstDeathQueue;
	}

	public void setxFirstDeathReason(String xFirstDeathReason) {
		this.xFirstDeathReason = xFirstDeathReason;
	}

}