package com.wingoku.market.services.productService;
import com.wingoku.market.models.Product;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.respositories.interfaces.IProductRepository;
import com.wingoku.market.services.base.BaseService;

public interface ProductService extends BaseService {
	public ResponseBody removeByProductSku(String title);
	public Product getProductById(int id);
	public Product getProductBySku(String sku); 
	public IProductRepository getRepo();
}
