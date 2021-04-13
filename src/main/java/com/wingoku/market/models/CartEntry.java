package com.wingoku.market.models;

import lombok.Data;

@Data
public class CartEntry {
	private String productName;
	private int orderedQuantity;
	private double price;
	private boolean discountApplied;
	private String discountMessage;
	
	public CartEntry() {}
	 
	public CartEntry(String productName, int orderedQuantity, double price, boolean discountApplied, String discountMessage) {
		this.productName = productName;
		this.orderedQuantity = orderedQuantity;
		this.price = price;
		this.discountApplied = discountApplied;
		this.discountMessage = discountMessage;
	}
}
