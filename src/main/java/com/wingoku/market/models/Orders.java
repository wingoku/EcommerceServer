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
@Table(name="orders")
public class Orders {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private double total;
	private boolean paid;
	private int quantity;
	@Column(name = "product_id")
	private int productId;
	
    public Orders() {}
     
    public Orders(int id, double total, boolean paid, int quantity, int productId) {
    	this.id = id;
    	this.total = total;
    	this.paid = paid;
    	this.quantity = quantity;
    	this.productId = productId;
	}
}
