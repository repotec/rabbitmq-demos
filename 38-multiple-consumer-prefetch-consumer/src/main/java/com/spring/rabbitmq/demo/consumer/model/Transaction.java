package com.spring.rabbitmq.demo.consumer.model;

import java.time.LocalDate;

/**
 * 
 *  @author ahmed
 * 
 */

public class Transaction {
	private long transaction_id;
	private LocalDate transaction_date;
	private Integer quantity;
	private Double taxAmount;
	
	public Transaction() {
		super();
	}

	
	public Transaction(long transaction_id, LocalDate transaction_date, Integer quantity, Double taxAmount) {
		super();
		this.transaction_id = transaction_id;
		this.transaction_date = transaction_date;
		this.quantity = quantity;
		this.taxAmount = taxAmount;
	}


	public Integer getQuantity() {
		return quantity;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public LocalDate getTransaction_date() {
		return transaction_date;
	}

	public long getTransaction_id() {
		return transaction_id;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public void setTransaction_date(LocalDate transaction_date) {
		this.transaction_date = transaction_date;
	}
	
	public void setTransaction_id(long transaction_id) {
		this.transaction_id = transaction_id;
	}


	@Override
	public String toString() {
		return "Transaction [transaction_id=" + transaction_id + ", transaction_date=" + transaction_date
				+ ", quantity=" + quantity + ", taxAmount=" + taxAmount + "]";
	}
}
