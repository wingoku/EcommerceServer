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
import com.wingoku.market.controllers.DealsController;
import com.wingoku.market.controllers.OrderController;
import com.wingoku.market.controllers.ProductController;
import com.wingoku.market.models.Cart;
import com.wingoku.market.models.CartEntry;
import com.wingoku.market.models.Deals;
import com.wingoku.market.models.Orders;
import com.wingoku.market.models.Product;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.services.dealsService.DealsService;
import com.wingoku.market.services.orderService.OrderService;
import com.wingoku.market.services.productService.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = DealsController.class)
@WithMockUser
public class DealsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DealsService dealsService;

	private Deals mockDeal;

	final String exampleDealsJSON = "{\r\n" + 
			"        \"discountPrice\" : 900,\r\n" + 
			"        \"productId\" : 1,\r\n" + 
			"    	\"quantity\" : 2\r\n" + 
			"    }";

	private ObjectWriter ow;
	 
	@BeforeEach
	public void init() {
		log.info("inside init()");;
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ow = mapper.writer().withDefaultPrettyPrinter();
	    
	    mockDeal = new Deals(1, 10, 100, 3);
	}
	
	@Test
	public void addDealTest() throws Exception {
		ResponseBody mockResponseBody = new ResponseBody(true, "Product: ....");
		Mockito.when(dealsService.add(mockDeal)).thenReturn(mockResponseBody);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/deals/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsBytes(mockDeal))
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		log.info("----> TEST::addDealTest(): response: {}", result.getResponse().getContentAsString());
		String expected = ow.writeValueAsString(mockResponseBody);

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
	
	@Test
	public void updateDealTest() throws Exception {
		ResponseBody mockResponseBody = new ResponseBody(true, "Product: ....");
		Mockito.when(dealsService.update(mockDeal)).thenReturn(mockResponseBody);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/deals/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsBytes(mockDeal))
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		log.info("----> TEST::updateDealTest(): response: {}", result.getResponse().getContentAsString());
		String expected = ow.writeValueAsString(mockResponseBody);

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}

	@Test
	public void getDealByProductIdTest() throws Exception {
		Mockito.when(dealsService.getDealObjectByProductId(1)).thenReturn(mockDeal);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/deals/getDealByProductId")
				.param("productId", "1")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		log.info("----> TEST::getDealByProductIdTest(): response: {}", result.getResponse().getContentAsString());
		String expected = ow.writeValueAsString(mockDeal);

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
}
