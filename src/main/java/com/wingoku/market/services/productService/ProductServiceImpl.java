package com.wingoku.market.services.productService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wingoku.market.models.Orders;
import com.wingoku.market.models.Product;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.respositories.interfaces.IProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	IProductRepository productRepository;
	
	@Override
	public Product getProductById(int id) {
		return productRepository.findById(id).orElse(null);
	}
 
	@Override
	public Product getProductBySku(String sku) {
		return productRepository.findBySku(sku);
	}

	@Override
	public IProductRepository getRepo() {
		return productRepository;
	}

	@Override
	public <T> ResponseBody update(T pro) {
		Product product = (Product) pro;
		if(productRepository.save(product) != null)
			return new ResponseBody(true, "Product: "+ product.getSku() + " existed. Update Performed");
		
		return new ResponseBody(false, "Updating Product: "+ product.getSku() + " failed");
	}

	@Override
	public <T> ResponseBody remove(T prod)  {
		Product product = (Product)prod;
		productRepository.delete(product);
		return new ResponseBody(true, "Product id: "+ product.getId() + " title: "+ product.getSku() + " removed");
	}

	@Override
	public ResponseBody removeByProductSku(String sku) {
		return remove(productRepository.findBySku(sku));
	}

	@Override
	public <T> ResponseBody add(T object) {
		log.info("inside add for product");
		Product product = (Product) object;
		Product productFromDB = productRepository.findBySku(product.getSku());

		if(productFromDB != null) {
			log.info("product with title: "+ product.getSku() + " already exists. Calling update in DB");
			productFromDB.setSku(product.getSku());
			productFromDB.setDescription(product.getDescription());
			productFromDB.setPrice(product.getPrice());
			
			return update(productFromDB);
		}
		
		if(productRepository.save(product) != null)
			return new ResponseBody(true, "Product: "+ product.getSku() + " added successfully");
		
		return new ResponseBody(false, "Adding Product: "+ product.getSku() + " failed");
	}
}
