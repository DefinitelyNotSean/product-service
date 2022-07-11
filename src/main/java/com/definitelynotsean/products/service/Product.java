package com.definitelynotsean.products.service;

import org.hibernate.validator.constraints.NotBlank;

public class Product {
	
	@NotBlank(message = "name is mandatory")
	private String name;
	
	@NotBlank(message = "name is mandatory")
	private String description;
	
	@NotBlank(message = "name is mandatory")
	private String price;
	
	@NotBlank(message = "name is mandatory")
	private String quantity;
	
	@NotBlank(message = "name is mandatory")
	private String size;
	
	@NotBlank(message = "name is mandatory")
	private String sku;	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}

}
