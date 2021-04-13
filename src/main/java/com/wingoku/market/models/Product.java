package com.wingoku.market.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="products")
public class Product {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
    private String sku;
    private String description;
    private double price;

    public Product() {}
     
    public Product(int id, String sku, String description, double price) {
    	this.id = id;
    	this.sku = sku;
    	this.description = description;
    	this.price = price;
	}
}
