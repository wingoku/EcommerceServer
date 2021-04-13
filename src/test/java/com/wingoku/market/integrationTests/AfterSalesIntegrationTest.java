package com.wingoku.market.integrationTests;

import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.wingoku.market.AfterSaleMarketPlaceApplication;
import com.wingoku.market.models.Cart;
import com.wingoku.market.models.CartEntry;
import com.wingoku.market.models.Deals;
import com.wingoku.market.models.Orders;
import com.wingoku.market.models.Product;
import com.wingoku.market.models.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = AfterSaleMarketPlaceApplication.class
	   )
public class AfterSalesIntegrationTest {
 
	@LocalServerPort
	private int port;
	HttpHeaders headers = new HttpHeaders();
	TestRestTemplate restTemplate = new TestRestTemplate();

	private ObjectWriter ow;
	private ObjectMapper mapper;
	
	private CartEntry entry1;
	private CartEntry entry2;
	private List<CartEntry> cartEntryList;
	private Cart cart;
	
	@BeforeEach
	public void init() {
		mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    mapper.disable(SerializationFeature.INDENT_OUTPUT);
	    ow = mapper.writer().withDefaultPrettyPrinter();
	    
	    entry1 = new CartEntry("A", 3, 100, true, "Special Offer: 3 items of A cost 100");
		entry2 = new CartEntry("C", 1, 25, false, "");

		cart = new Cart(125, 4, true, cartEntryList);
		
		cartEntryList = (List<CartEntry>) (List<?>) Arrays.asList(new CartEntry[] {
				entry1, entry2
		});
	}

	@Test
	public void addProducts() throws Exception {
	    List<Product> productsList = (List<Product>) (List<?>) Arrays.asList(new Product[] {new Product(100, "A", "Product A Description", 40),
	    		new Product(101, "B", "Product B Description", 80),
	    		new Product(102, "C", "Product C Description", 25),
	    		new Product(103, "D", "Product D Description", 20)
	    		});

		ResponseBody responseBody = new ResponseBody(true, "Product: {} added/updated");
		
		//add all products
		for(Product product : productsList)
			addProduct(responseBody, product);
	}
	
	private void addProduct(ResponseBody responseBody, Product product) throws Exception {
		HttpEntity<Product> entity = new HttpEntity<Product>(product, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/products/add"),
				HttpMethod.POST, entity, String.class);

		String expected = ow.writeValueAsString(responseBody);

		log.info("response.getBody(): "+response.getBody());
		//assert if success == true. Be lenient about message content
		JSONAssert.assertEquals(expected, response.getBody(), new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("message", (o1, o2) -> true)));
	}
	

	@Test
	public void addDeals() throws Exception {
														//(int id, int productId, double discountPrice, int quantity) 
	    List<Deals> dealsList = (List<Deals>) (List<?>) Arrays.asList(new Deals[] {new Deals(1, 32, 100, 3)});

		ResponseBody responseBody = new ResponseBody(true, "Deal for product: {} added/updated");
		
		for(Deals deal : dealsList)
			addDeal(responseBody, deal);
	}
	
	private void addDeal(ResponseBody responseBody, Deals deal) throws Exception {
		HttpEntity<Deals> entity = new HttpEntity<Deals>(deal, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/deals/add"),
				HttpMethod.POST, entity, String.class);

		String expected = ow.writeValueAsString(responseBody);

		log.info("response.getBody(): "+response.getBody());

		//assert if success == true. Be lenient about message content
		JSONAssert.assertEquals(expected, response.getBody(), new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("message", (o1, o2) -> true)));
	}
	
	@Test
	public void getProductBySKu() throws Exception {
		Product responseProduct = new Product(100, "A", "Product A Description", 40);
		
		String responseBody = getProductBySKu(responseProduct.getSku());
		String expected = ow.writeValueAsString(responseProduct);

		log.info("response body: "+ responseBody);
		
		JSONAssert.assertEquals(expected, responseBody, new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("id", (o1, o2) -> true),
                new Customization("description", (o1, o2) -> true),
                new Customization("price", (o1, o2) -> true)));
	}

	private String getProductBySKu(String productSKU) throws Exception {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/products/getProductBySku?sku={sku}"),
				HttpMethod.GET, entity, String.class, productSKU);
		
		return response.getBody(); 
	}

	@Test
	public void addOrders() throws Exception {
		int productAID = mapper.readValue(getProductBySKu("A"), Product.class).getId();
		int productCID = mapper.readValue(getProductBySKu("C"), Product.class).getId();
		
		log.info("product A: "+ productAID + " productC: "+ productCID);

																		//(int id, double total, boolean paid, int quantity, int productId)
	    List<Orders> ordersList = (List<Orders>) (List<?>) Arrays.asList(new Orders[] {new Orders(1, 100.0, true, 3, productAID),
	    		new Orders(2, 25.0, true, 1, productCID)});

		ResponseBody responseBody = new ResponseBody(true, "Order for product: {} added/updated");
		
		for(Orders deal : ordersList)
			addOrder(responseBody, deal);
	}
	
	private void addOrder(ResponseBody responseBody, Orders order) throws Exception {
		HttpEntity<Orders> entity = new HttpEntity<Orders>(order, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/order/add"),
				HttpMethod.POST, entity, String.class);

		String expected = ow.writeValueAsString(responseBody);

		log.info("response.getBody(): "+response.getBody());

		//assert if success == true. Be lenient about message content
		JSONAssert.assertEquals(expected, response.getBody(), new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("message", (o1, o2) -> true)));
	}
	
	@Test
	public void getAllOrders() throws Exception {
		String responseBody = getAllOrdersInCart();

		String expected = ow.writeValueAsString(cart);

		log.info("response body: "+ responseBody);
		
		//assert if success == true. Be lenient about cartEntryList content
		JSONAssert.assertEquals(expected, responseBody, new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("cartEntryList", (o1, o2) -> true)));
	}
	
	private String getAllOrdersInCart() throws Exception {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/order/getAllOrdersInCart"),
				HttpMethod.GET, entity, String.class);
		
		return response.getBody(); 
	}
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
