package com.wingoku.market.controllers;

import org.springframework.http.ResponseEntity;

import com.wingoku.market.models.ResponseBody;

public class BaseController {
	
	//create success/failure response based on the responseBody 
	protected ResponseEntity<?> getResponseEntityObject(ResponseBody responseBody) {
		if(responseBody.isSuccess())
			return ResponseEntity.ok(responseBody);
		
		return ResponseEntity.badRequest().body(responseBody);
	}
}
