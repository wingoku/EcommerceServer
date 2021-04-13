package com.wingoku.market.unitTests;

import java.nio.charset.Charset;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.wingoku.market.controllers.OrderController;
import com.wingoku.market.controllers.ProductController;
import com.wingoku.market.models.Cart;
import com.wingoku.market.models.CartEntry;
import com.wingoku.market.models.Orders;
import com.wingoku.market.models.Product;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.services.orderService.OrderService;
import com.wingoku.market.services.productService.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = OrderController.class)
@WithMockUser
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;
	private ObjectWriter ow;

	private Orders mockOrder;
	private Orders mockOrder2;
	
	private CartEntry entry1;
	private CartEntry entry2;
	private List<CartEntry> cartEntryList;
	
	private Cart mockCart;

	final String exampleOrderJSON = "{\r\n" + 
			"        \"total\" : 1000,\r\n" + 
			"        \"paid\": true,\r\n" + 
			"        \"productId\" : 2,\r\n" + 
			"    	\"quantity\" : 1\r\n" + 
			"    }";

	@BeforeEach
	public void initJackson() {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ow = mapper.writer().withDefaultPrettyPrinter();
	    
		mockOrder = new Orders(10, 100, true, 3, 200);
		mockOrder2 = new Orders(11, 500, true, 1, 201);
		
		entry1 = new CartEntry("A", 3, 100, true, "Special Offer: 3 items of A cost 100");
		entry2 = new CartEntry("B", 1, 500, false, "");

		mockCart = new Cart(1500, 2, true, cartEntryList);
		
		cartEntryList = (List<CartEntry>) (List<?>) Arrays.asList(new CartEntry[] {
				entry1, entry2
		});
	}
 
	@Test
	public void addOrderTest() throws Exception {
		ResponseBody mockResponseBody = new ResponseBody(true, "Order for Product: ....");
		Mockito.when(orderService.add(mockOrder)).thenReturn(mockResponseBody);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/order/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsBytes(mockOrder))
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		log.info("----> TEST::addOrderTest(): response: {}", result.getResponse().getContentAsString());
		String expected = ow.writeValueAsString(mockResponseBody);

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
	
	@Test
	public void updateOrderTest() throws Exception {
		ResponseBody mockResponseBody = new ResponseBody(true, "Order for Product: ....");
		Mockito.when(orderService.update(mockOrder)).thenReturn(mockResponseBody);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/order/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsBytes(mockOrder))
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		log.info("----> TEST::updateProductTest(): response: {}", result.getResponse().getContentAsString());

		String expected = ow.writeValueAsString(mockResponseBody);
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}

	@Test
	public void getAllOrdersTest() throws Exception {
		Mockito.when(orderService.getAllOrdersInCart()).thenReturn(mockCart);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/order/getAllOrdersInCart")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		log.info("----> TEST::getAllOrdersTest(): response: {}", result.getResponse().getContentAsString());
		String expected = ow.writeValueAsString(mockCart);

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
}
