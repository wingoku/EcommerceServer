package com.wingoku.market.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.wingoku.market.models.Product;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.respositories.interfaces.IProductRepository;
import com.wingoku.market.services.productService.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/api/products")
public class ProductController extends BaseController {
	@Autowired
	ProductService productService;
	
	@GetMapping("/all")
	public List<Product> getAll() {
		return productService.getRepo().findAll();
	}
	
	@PostMapping("/add") 
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		log.info("addProduct(): "+ product.getDescription() + " "+ product.getSku());
		ResponseBody response = productService.add(product);
		return getResponseEntityObject(response);
	}
	
	@GetMapping("/getProductBySku")
	public ResponseEntity<?> getProductBySku(@RequestParam String sku) {
		log.info("getProductBySku() product controller for SKU: "+ sku);
		Product product = productService.getProductBySku(sku);
		
		if(product == null)
			return getResponseEntityObject(new ResponseBody(false, "Product doesn't exist"));
		
		return ResponseEntity.ok(product);
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updateProduct(@RequestBody Product product) {
		ResponseBody response = productService.update(product);
		return getResponseEntityObject(response);
	}
	
	@GetMapping("/removeByTitle")
	public ResponseEntity<?> removeProductByTitle(@RequestParam String title) {
		ResponseBody response = productService.removeByProductSku(title);
		return getResponseEntityObject(response);
	}
}
