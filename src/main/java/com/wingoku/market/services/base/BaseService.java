package com.wingoku.market.services.base;

import com.wingoku.market.models.ResponseBody;

public interface BaseService {
	public <T> ResponseBody add(T add); 
	public <T> ResponseBody update(T update);
	public <T> ResponseBody remove(T remove);
}
