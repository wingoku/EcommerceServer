package com.wingoku.market.respositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wingoku.market.models.Deals;

public interface IDealsRepository extends JpaRepository<Deals, Integer> {
	public Deals findByProductId(int productId);
	public boolean existsByProductId(int productId);
}
