package com.wingoku.market.services.dealsService;


import com.wingoku.market.models.Deals;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.respositories.interfaces.IDealsRepository;
import com.wingoku.market.services.base.BaseService;

public interface DealsService extends BaseService{
	public ResponseBody removeByProductId(int productId);
	public boolean doesDealExistWithProductId(int productId);
	public Deals getDealObjectByProductId(int productId);
	public IDealsRepository getRepo(); 
}
