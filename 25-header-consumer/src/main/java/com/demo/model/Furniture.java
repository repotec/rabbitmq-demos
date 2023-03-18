package com.demo.model;

public class Furniture {
	private String material;
	private String color;
	private String name;
	private int price;
	
	public String getMaterial() {
		return material;
	}
	public String getName() {
		return name;
	}
	public String getColor() {
		return color;
	}
	public int getPrice() {
		return price;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "Furniture [material=" + material + ", color=" + color + ", name=" + name + ", price=" + price + "]";
	}
}
