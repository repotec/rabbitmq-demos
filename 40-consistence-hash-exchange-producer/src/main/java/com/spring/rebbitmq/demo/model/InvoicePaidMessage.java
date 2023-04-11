package com.spring.rebbitmq.demo.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InvoicePaidMessage {
	private String invoiceNumber;
	
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate paidDate;
	
	private String paymentNumber;

	public InvoicePaidMessage() {
		super();
	}

	public InvoicePaidMessage(String invoiceNumber, LocalDate paidDate, String paymentNumber) {
		super();
		this.invoiceNumber = invoiceNumber;
		this.paidDate = paidDate;
		this.paymentNumber = paymentNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public LocalDate getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(LocalDate paidDate) {
		this.paidDate = paidDate;
	}

	public String getPaymentNumber() {
		return paymentNumber;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	@Override
	public String toString() {
		return "InvoicePaidMessage [invoiceNumber=" + invoiceNumber + ", paidDate=" + paidDate + ", paymentNumber="
				+ paymentNumber + "]";
	}
	
	
}
