package com.wingoku.market.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="deals")
public class Deals {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "product_id")
	private int productId;
	@Column(name = "discount_price")
	private double discountPrice;
	private int quantity;
	
	public Deals() {}
	 
	public Deals(int id, int productId, double discountPrice, int quantity) {
		this.id = id;
		this.productId = productId;
		this.discountPrice = discountPrice;
		this.quantity = quantity;
	}
}
