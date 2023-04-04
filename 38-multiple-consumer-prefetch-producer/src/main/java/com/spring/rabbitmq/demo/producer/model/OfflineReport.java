package com.spring.rabbitmq.demo.producer.model;

import java.time.LocalDate;

public class OfflineReport {
	private Integer numOfTransaction;
	private LocalDate dateFrom;
	private LocalDate dateTo;
	private Double totalAmount;
	
	public OfflineReport() {
		super();
	}
	
	public OfflineReport(Integer numOfTransaction, LocalDate dateFrom, LocalDate dateTo, Double totalAmount) {
		super();
		this.numOfTransaction = numOfTransaction;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.totalAmount = totalAmount;
	}
	public Integer getNumOfTransaction() {
		return numOfTransaction;
	}
	public void setNumOfTransaction(Integer numOfTransaction) {
		this.numOfTransaction = numOfTransaction;
	}
	public LocalDate getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}
	public LocalDate getDateTo() {
		return dateTo;
	}
	public void setDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	@Override
	public String toString() {
		return "OfflineReport [numOfTransaction=" + numOfTransaction + ", dateFrom=" + dateFrom + ", dateTo=" + dateTo
				+ ", totalAmount=" + totalAmount + "]";
	}
}
