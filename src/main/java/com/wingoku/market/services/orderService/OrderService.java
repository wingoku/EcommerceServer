package com.wingoku.market.services.orderService;

import java.util.List;

import com.wingoku.market.models.Cart;
import com.wingoku.market.models.CartEntry;
import com.wingoku.market.models.Orders;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.respositories.interfaces.IDealsRepository;
import com.wingoku.market.respositories.interfaces.IOrderRepository;
import com.wingoku.market.services.base.BaseService;

public interface OrderService extends BaseService{
	public ResponseBody removeByProductId(int productId); 
	public Cart getAllOrdersInCart();
	public double getDiscountPrice(int orderedQuantity, int discountQuantity, double regularPrice, double discountPrice);
	public IOrderRepository getRepo();
}
