package com.wingoku.market.services.orderService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.wingoku.market.models.Cart;
import com.wingoku.market.models.CartEntry;
import com.wingoku.market.models.Deals;
import com.wingoku.market.models.Orders;
import com.wingoku.market.models.Product;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.respositories.interfaces.IOrderRepository;
import com.wingoku.market.services.dealsService.DealsService;
import com.wingoku.market.services.productService.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	IOrderRepository orderRepository;
	@Autowired
	ProductService productService;
	@Autowired
	DealsService dealsService;
	
	@Override
	public double getDiscountPrice(int orderedQuantity, int discountQuantity, double regularPrice, double discountPrice) {
		double remainder = (orderedQuantity*1.0) / (discountQuantity*1.0);
	
		log.info("orderedQuantity: "+ orderedQuantity);
		log.info("discountQuantity: "+ discountQuantity);
		log.info("inside getDiscountPrice: remainder: "+ remainder);
		log.info("inside getDiscountPrice: discountPrice: "+ (Math.floor(remainder) * discountPrice));
		log.info("inside getDiscountPrice: regularPrice: "+ (Math.ceil(remainder - (int)remainder) * Math.abs(orderedQuantity-discountQuantity) * regularPrice));
		log.info("inside getDiscountPrice: totalPrice: "+ (Math.floor(remainder) * discountPrice + Math.ceil(remainder - (int)remainder) * Math.abs(orderedQuantity-discountQuantity) * regularPrice));
		
		//calculate sum of discountPrice & regular price based on the quantity
														//get INT part from decimal remainder  //quantity not part of discount/deal
		return Math.floor(remainder) * discountPrice + Math.ceil(remainder - (int)remainder) * Math.abs(orderedQuantity-discountQuantity) * regularPrice;
	}

	@Override
	public IOrderRepository getRepo() {
		return orderRepository;
	}

	@Override
	public <T> ResponseBody add(T ord) {
		Orders order = (Orders) ord;
		if(!productService.getRepo().existsById(order.getProductId()))
			return new ResponseBody(false, "Order for product: "+order.getProductId()+ " can't be placed. Product doesn't exist");
		
		log.info("productID: "+ order.getProductId() + " totalPrice: "+ order.getTotal() + " quantity: "+ order.getQuantity() + " paid: "+ order.isPaid());

		Orders orderFromDB = orderRepository.findByProductId(order.getProductId());

		if (orderFromDB != null) {
			log.info(
					"order with product_id: " + order.getProductId() + " already exists. Calling update in DB");
			
			orderFromDB.setPaid(order.isPaid());
			orderFromDB.setProductId(order.getProductId());
			orderFromDB.setQuantity(order.getQuantity());
			orderFromDB.setTotal(order.getTotal());
			return update(orderFromDB);
		}

		if(orderRepository.save(order) != null)
			return new ResponseBody(true, "Order for product: "+order.getProductId()+ " added successfully");

		return new ResponseBody(false, "Adding order for product: "+ order.getProductId() + " failed");
	}

	@Override
	public <T> ResponseBody update(T or) {
		Orders order = (Orders) or;
		if(orderRepository.save(order) != null)
			return new ResponseBody(true, "Order for Product: "+ order.getProductId() + " existed. Update Performed");
		
		return new ResponseBody(false, "Updating order for product: "+ order.getProductId() + " failed");
	}

	@Override
	public <T> ResponseBody remove(T ord) {
		Orders order = (Orders)ord;
		orderRepository.delete(order);
		return new ResponseBody(true, "Order removed for product: "+order.getProductId());
	}

	@Override
	public ResponseBody removeByProductId(int productId) {
		return remove(orderRepository.findByProductId(productId));
	}

	@Override
	public Cart getAllOrdersInCart() {
		Cart cart = new Cart();
		List<CartEntry> cartList = new ArrayList<CartEntry>();
		List<Orders> ordersList = orderRepository.findAll();

		log.info("inside getAllOrdersInCart: totalOrders: "+ ordersList.size());
		for(Orders order : ordersList) {
			//get productObjects that're in the orderList
			//check if deal exists for a product in orderList
			//check if order quantity for the deal product matches the quantity in deals menu
			
			CartEntry cartEntry = new CartEntry();
			
			Product product = productService.getRepo().findById(order.getProductId()).orElse(null);
			
			if(product == null) {
				log.info("Can't add order with product id: "+ order.getProductId());
				continue;
			}

			cartEntry.setProductName(product.getSku());
			cartEntry.setOrderedQuantity(order.getQuantity());
			
			cart.setQuantity(cart.getQuantity()+order.getQuantity());
			
			//if deal exists for the product, calculate discount based on deal
			if(dealsService.doesDealExistWithProductId(order.getProductId())) {
				Deals deal = dealsService.getDealObjectByProductId(order.getProductId());
				
				double adjustedDiscountPrice = getDiscountPrice(order.getQuantity(), deal.getQuantity(), product.getPrice(), deal.getDiscountPrice());
				cartEntry.setDiscountApplied((adjustedDiscountPrice != product.getPrice()));
				String discountMessage = "Buy "+ deal.getQuantity() + " "+ product.getSku() + " for "+ deal.getDiscountPrice();
				cartEntry.setDiscountMessage(discountMessage);
				cartEntry.setPrice(adjustedDiscountPrice);
				
				cart.setTotalPrice(cart.getTotalPrice()+adjustedDiscountPrice);
				cart.setDiscountApplied(true);
			}
			else {
				cartEntry.setPrice(order.getQuantity() * product.getPrice());
				cart.setTotalPrice(cart.getTotalPrice() + order.getQuantity() * product.getPrice());
			}
			
			cartList.add(cartEntry);	
		}
		
		cart.setCartEntryList(cartList);
		
		return cart;
	}
}
