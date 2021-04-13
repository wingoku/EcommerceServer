package com.wingoku.market.respositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wingoku.market.models.Product;

public interface IProductRepository extends JpaRepository<Product, Integer> {
	public Product findBySku(String title); 
}
