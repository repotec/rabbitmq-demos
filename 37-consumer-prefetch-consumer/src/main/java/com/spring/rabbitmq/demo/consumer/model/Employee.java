package com.spring.rabbitmq.demo.consumer.model;

import java.time.LocalDate;

/**
 * 
 *  @author ahmed
 * 
 */

public class Employee {
	private long id;
	private String name;
	private LocalDate birthOfdate;
	
	public Employee() {
		super();
	}

	public Employee(long id, String name, LocalDate birthOfdate) {
		super();
		this.id = id;
		this.name = name;
		this.birthOfdate = birthOfdate;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getBirthOfdate() {
		return birthOfdate;
	}
	public void setBirthOfdate(LocalDate birthOfdate) {
		this.birthOfdate = birthOfdate;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", birthOfdate=" + birthOfdate + "]";
	}
}
