package com.wingoku.market.services.dealsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.wingoku.market.models.Deals;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.respositories.interfaces.IDealsRepository;
import com.wingoku.market.services.productService.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DealsServiceImpl implements DealsService {

	@Autowired
	IDealsRepository dealsRepository;
	
	@Autowired
	ProductService productService; 
	
	@Override
	public Deals getDealObjectByProductId(int productId) {
		return dealsRepository.findByProductId(productId);
	}

	@Override
	public boolean doesDealExistWithProductId(int productId) {
		return dealsRepository.existsByProductId(productId);
	}

	@Override
	public IDealsRepository getRepo() {
		return dealsRepository;
	}

	@Override
	public <T> ResponseBody add(T aDeal) {
		Deals deal = (Deals) aDeal;
		if(!productService.getRepo().existsById(deal.getProductId()))
			return new ResponseBody(true, "Product: "+deal.getProductId()+ " doesn't exist. Deal not added");
		
		Deals dealFromDB = dealsRepository.findByProductId(deal.getProductId());

		if (dealFromDB != null) {
			log.info(
					"deal with product_id: " + dealFromDB.getProductId() + " already exists. Calling update in DB");

			dealFromDB.setDiscountPrice(deal.getDiscountPrice());
			dealFromDB.setProductId(deal.getProductId());
			dealFromDB.setQuantity(deal.getQuantity());
			return update(dealFromDB);
		}
		
		if(dealsRepository.save(deal) != null)
			return new ResponseBody(true, "Deal for product: "+deal.getProductId()+ " added successfully");

		return new ResponseBody(false, "Adding Deal for product: "+deal.getProductId() + " failed");
	}

	@Override
	public <T> ResponseBody update(T aDeal) {
		Deals deal = (Deals) aDeal;
		if(!dealsRepository.existsById(deal.getProductId()))
			return new ResponseBody(true, "Deal can't be added for product: "+deal.getProductId()+ ". Product doesn't exist");

		if(dealsRepository.save(deal) != null)
			return new ResponseBody(true, "Deal updated for product: "+deal.getProductId());

		return new ResponseBody(false, "Deal update for product: "+deal.getProductId() + " failed");
	}

	@Override
	public <T> ResponseBody remove(T dealToBeRemoved) {
		Deals deal = (Deals)dealToBeRemoved;
		dealsRepository.delete(deal);
		return new ResponseBody(true, "Deal remove for product: "+deal.getProductId());
	}

	@Override
	public ResponseBody removeByProductId(int productId) {
		return remove(dealsRepository.findByProductId(productId));
	}
}
