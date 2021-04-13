package com.wingoku.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wingoku.market.models.Orders;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.services.orderService.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/order")
public class OrderController extends BaseController {
	@Autowired
	OrderService orderService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addOrder(@RequestBody Orders order) {
		ResponseBody response = orderService.add(order);
		return getResponseEntityObject(response);
	}
	
	@PostMapping("/update") 
	public ResponseEntity<?> updateOrder(@RequestBody Orders order) {
		ResponseBody response = orderService.update(order);
		return getResponseEntityObject(response);
	}

	@GetMapping("/removeByProductId")
	public ResponseEntity<?> removeByProductId(@RequestParam int productId) {
		ResponseBody response = orderService.removeByProductId(productId);
		return getResponseEntityObject(response);
	}
	
	@GetMapping("/getAllOrdersInCart")
	public ResponseEntity<?> getAllOrdersInCart() {
		return ResponseEntity.ok(orderService.getAllOrdersInCart());
	}
}
