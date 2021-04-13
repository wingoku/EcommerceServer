package com.wingoku.market.models;

import lombok.Data;

@Data
public class ResponseBody {
	private boolean isSuccess;
	private String message;
	 
	public ResponseBody(boolean isSuccess, String message) {
		this.isSuccess = isSuccess;
		this.message = message;
	}
}
