package com.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitSchemaConfig {
    
	@Bean
	public Declarables rabbitSchema() {
//		Queue Q_DISCOUNT = new Queue("q.promotion.discount", false);
//	    Queue Q_FREE_DELIVERY = new Queue("q.promotion.free-delivery", false);
//	    HeadersExchange X_PROMOTION = new HeadersExchange("x.promotion.header", true, false);	
//		return new Declarables(Q_DISCOUNT,
//							   Q_FREE_DELIVERY,
//							   X_PROMOTION,
//							   BindingBuilder.bind(Q_DISCOUNT).to(X_PROMOTION).whereAll(x).equals(x),
//							   BindingBuilder.bind(Q_DISCOUNT).to(X_PROMOTION).where("material").matches("wood"),
//							   BindingBuilder.bind(Q_DISCOUNT).to(X_PROMOTION).where("color").matches("red"),
//							   BindingBuilder.bind(Q_DISCOUNT).to(X_PROMOTION).where("material").matches("steel"),
//							   
//							   BindingBuilder.bind(Q_FREE_DELIVERY).to(X_PROMOTION).where("color").matches("white"),
//							   BindingBuilder.bind(Q_FREE_DELIVERY).to(X_PROMOTION).where("material").matches("wood"),
//							   BindingBuilder.bind(Q_FREE_DELIVERY).to(X_PROMOTION).where("x-match").matches("any"));

		String X_PROMOTION = "x.promotion.header";
		String Q_DISCOUNT = "q.promotion.discount";
		String Q_FREE_DELIVERY = "q.promotion.free-delivery";
		return new Declarables(new HeadersExchange(X_PROMOTION),
		
					   new Queue(Q_DISCOUNT),
					   new Binding(Q_DISCOUNT, DestinationType.QUEUE, X_PROMOTION, "", new HashMap<String, Object>() {{
																															put("color", "white"); 
																															put("material", "wood");
																													   }}),
					   
					   new Binding(Q_DISCOUNT, DestinationType.QUEUE, X_PROMOTION, "", new HashMap<String, Object>() {{
																															put("color", "red"); 
																															put("material", "steel");
																													   }}),
					   
					   new Queue(Q_FREE_DELIVERY),
					   new Binding(Q_FREE_DELIVERY, DestinationType.QUEUE, X_PROMOTION, "", new HashMap<String, Object>() {{
																																put("color", "red"); 
																																put("material", "wood");
																																put("x-match", "any");
																													   		}}));
	}
}
