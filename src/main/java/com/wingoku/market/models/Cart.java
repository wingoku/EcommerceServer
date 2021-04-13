package com.wingoku.market.models;

import java.util.List;

import lombok.Data;

@Data
public class Cart {

	private double totalPrice;
	private int quantity;
	private boolean discountApplied;
	private List<CartEntry> cartEntryList;
	
	public Cart() {}
	 
	public Cart(double totalPrice, int quantity, boolean discountApplied, List<CartEntry> cartEntryList) {
		this.totalPrice = totalPrice;
		this.quantity = quantity;
		this.discountApplied = discountApplied;
		this.cartEntryList = cartEntryList;
	}
}
