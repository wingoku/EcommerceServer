package com.wingoku.market.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wingoku.market.models.Deals;
import com.wingoku.market.models.Orders;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.respositories.interfaces.IDealsRepository;
import com.wingoku.market.respositories.interfaces.IOrderRepository;
import com.wingoku.market.respositories.interfaces.IProductRepository;
import com.wingoku.market.services.dealsService.DealsService;
import com.wingoku.market.services.productService.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/deals")
public class DealsController extends BaseController {
	@Autowired
	DealsService dealsService;
	
	@GetMapping("/all")
	public List<Deals> getAll() {
		return dealsService.getRepo().findAll();
	}

	@PostMapping("/add") 
	public ResponseEntity<?> addDeal(@RequestBody Deals deal) {
		log.info("add deal product id: "+ deal.getProductId() + " discountPrice: "+ deal.getDiscountPrice());		

		ResponseBody response = dealsService.add(deal);
		return getResponseEntityObject(response);
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updateDeal(@RequestBody Deals deal) {
		ResponseBody response = dealsService.update(deal);
		return getResponseEntityObject(response);
	}

	@GetMapping("/getDealByProductId")
	public ResponseEntity<?> getDealByProductId(@RequestParam int productId) {
		Deals deal = dealsService.getDealObjectByProductId(productId);
		if(deal == null)
			return getResponseEntityObject(new ResponseBody(false, "Deal doesn't exist for product id: "+ productId));
		return ResponseEntity.ok(dealsService.getDealObjectByProductId(productId));
	}

	@GetMapping("/removeByProductId")
	public ResponseEntity<?> removeByProductId(@RequestParam int productId) {
		ResponseBody response = dealsService.removeByProductId(productId);
		return getResponseEntityObject(response);
	}
}
