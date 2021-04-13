package com.wingoku.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@SpringBootApplication
public class AfterSaleMarketPlaceApplication {

	public static void main(String[] args) {
//		SpringApplication.run(AfterSaleMarketPlaceApplication.class, args);
		
		getDiscountPrice(5, 3, 40, 100);
		
		
	}
	
	public static double getDiscountPrice(int orderedQuantity, int discountQuantity, double regularPrice, double discountPrice) {
		int remainder = (orderedQuantity) / (discountQuantity);
	
		log.info("orderedQuantity: "+ orderedQuantity);
		log.info("discountQuantity: "+ discountQuantity);
		log.info("inside getDiscountPrice: remainder: "+ remainder);
		log.info("inside getDiscountPrice: discountPrice: "+ (Math.floor(remainder) * discountPrice));
		log.info("inside getDiscountPrice: regularPrice: "+ (Math.ceil(remainder - (int)remainder) * Math.abs(orderedQuantity-discountQuantity) * regularPrice));
		log.info("inside getDiscountPrice: totalPrice: "+ (Math.floor(remainder) * discountPrice + Math.ceil(remainder - (int)remainder) * Math.abs(orderedQuantity-discountQuantity) * regularPrice));
		
		log.info("new output: "+(remainder * discountPrice + (orderedQuantity % discountQuantity) * regularPrice));
		//calculate sum of discountPrice & regular price based on the quantity
														//get INT part from decimal remainder  //quantity not part of discount/deal
		return remainder * discountPrice + (orderedQuantity % discountQuantity) * regularPrice;
	}
}
