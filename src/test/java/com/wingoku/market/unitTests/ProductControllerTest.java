package com.wingoku.market.unitTests;

import java.nio.charset.Charset;

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
import com.wingoku.market.controllers.ProductController;
import com.wingoku.market.models.Product;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.services.productService.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = ProductController.class)
@WithMockUser
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;
	private ObjectWriter ow;

	private Product mockProduct;

	final String exampleProductJSON = "{\r\n" + 
			"        \"sku\": \"Google Pixel 5 XL\",\r\n" + 
			"        \"description\": \"Google Pixel 5 description\",\r\n" + 
			"        \"price\": 1199\r\n" + 
			"    }";
	
	 
	@BeforeEach
	public void initJackson() {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ow = mapper.writer().withDefaultPrettyPrinter();
	    
	    mockProduct = new Product(100, "A", "Product A Description", 0.40);
	}

	@Test
	public void addProductTest() throws Exception {
		ResponseBody mockResponseBody = new ResponseBody(true, "Product: ....");
		Mockito.when(productService.add(mockProduct)).thenReturn(mockResponseBody);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/products/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsBytes(mockProduct))
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		log.info("----> TEST::addProductTest(): response: {}", result.getResponse().getContentAsString());
		String expected = ow.writeValueAsString(mockResponseBody);

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
	
	@Test
	public void updateProductTest() throws Exception {
		ResponseBody mockResponseBody = new ResponseBody(true, "Product: ....");
		Mockito.when(productService.update(mockProduct)).thenReturn(mockResponseBody);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/products/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsBytes(mockProduct))
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		log.info("----> TEST::updateProductTest(): response: {}", result.getResponse().getContentAsString());
		String expected = ow.writeValueAsString(mockResponseBody);

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}

	@Test
	public void getProductBySKUTest() throws Exception {
		Mockito.when(productService.getProductBySku(Mockito.anyString())).thenReturn(mockProduct);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products/getProductBySku")
				.param("sku", mockProduct.getSku())
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		log.info("----> TEST::getProductBySKUTest(): response: {}", result.getResponse().getContentAsString());
		String expected = ow.writeValueAsString(mockProduct);

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
}
