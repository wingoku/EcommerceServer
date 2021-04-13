package com.wingoku.market.respositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wingoku.market.models.Orders;

public interface IOrderRepository extends JpaRepository<Orders, Integer> {
	public Orders findByProductId(int productID); 
}
