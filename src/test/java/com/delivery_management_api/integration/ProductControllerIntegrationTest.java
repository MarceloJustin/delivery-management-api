package com.delivery_management_api.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.delivery_management_api.entity.Product;
import com.delivery_management_api.entity.Restaurant;
import com.delivery_management_api.repository.ProductRepository;
import com.delivery_management_api.repository.RestaurantRepository;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ProductControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private ProductRepository productRepository;

	@Test
	void shouldReturnProductList() throws Exception {
		mockMvc.perform(get("/api/products")).andExpect(status().isOk());
	}

	@Test
	void shouldReturnNotFoundWhenProductDoesNotExists() throws Exception {
		mockMvc.perform(get("/api/products/99999")).andExpect(status().isNotFound());
	}

	@Test
	void shouldReturnProductWhenExisted() throws Exception {
		Restaurant restaurant = createRestaurant();
		Product product = createProduct(restaurant);

		mockMvc.perform(get("/api/products/" + product.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(product.getId()))
				.andExpect(jsonPath("$.name").value("Pizza 4Queijos"))
				.andExpect(jsonPath("$.price").value(38.80))
				.andExpect(jsonPath("$.restaurantId").value(restaurant.getId()))
				.andExpect(jsonPath("$.restaurantName").value("Pizzaria do Marcelo"));
	}
	
	@Test
	void shouldReturnProductByRestaurant() throws Exception {
		Restaurant restaurant = createRestaurant();
		Product product = createProduct(restaurant);

		mockMvc.perform(get("/api/products/restaurant/" + restaurant.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(product.getId()))
				.andExpect(jsonPath("$[0].name").value("Pizza 4Queijos"))
				.andExpect(jsonPath("$[0].price").value(38.80))
				.andExpect(jsonPath("$[0].restaurantId").value(restaurant.getId()))
				.andExpect(jsonPath("$[0].restaurantName").value("Pizzaria do Marcelo"));
	}

	@Test
	void shouldReturnProductsAbovePrice() throws Exception {
		Restaurant restaurant = createRestaurant();
		Product product = createProduct(restaurant);

		mockMvc.perform(get("/api/products/price/" + 30)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(product.getId()))
				.andExpect(jsonPath("$[0].name").value("Pizza 4Queijos"))
				.andExpect(jsonPath("$[0].price").value(38.80))
				.andExpect(jsonPath("$[0].restaurantId").value(restaurant.getId()))
				.andExpect(jsonPath("$[0].restaurantName").value("Pizzaria do Marcelo"));
	}
	
	@Test
	void shouldReturnProductsWithinPriceRange() throws Exception{
		Restaurant restaurant = createRestaurant();
		
		Product product1 = productRepository.save(new Product("Pizza Calabresa", BigDecimal.valueOf(20.00), restaurant));
		Product product2 = productRepository.save(new Product("Pizza Frango", BigDecimal.valueOf(40.00), restaurant));
		Product product3 = productRepository.save(new Product("Pizza 4Queijos", BigDecimal.valueOf(60.00), restaurant));

		mockMvc.perform(get("/api/products/price-range")
				.param("minPrice", "30")
                .param("maxPrice", "50")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(product2.getId()))
				.andExpect(jsonPath("$[0].name").value("Pizza Frango"))
				.andExpect(jsonPath("$[0].price").value(40.00))
				.andExpect(jsonPath("$[0].restaurantId").value(restaurant.getId()))
				.andExpect(jsonPath("$[0].restaurantName").value("Pizzaria do Marcelo"));
	}
	
	@Test
	void shouldCreateProduct() throws Exception {
		Restaurant restaurant = createRestaurant();

		String requestBody = """
				{
				"name":"Pizza 4Queijos",
				"price": 38.80,
				"restaurantId": %d
				}
				""".formatted(restaurant.getId());

		mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("Pizza 4Queijos"))
				.andExpect(jsonPath("$.price").value(38.80))
				.andExpect(jsonPath("$.restaurantId").value(restaurant.getId()))
				.andExpect(jsonPath("$.restaurantName").value("Pizzaria do Marcelo"));
		
		List<Product> products = productRepository.findAll();

		assertEquals(1, products.size());

		Product savedProduct = products.getFirst();

		assertEquals("Pizza 4Queijos", savedProduct.getName());
		assertEquals(0, new BigDecimal("38.80").compareTo(savedProduct.getPrice()));
		assertEquals(restaurant.getId(),savedProduct.getRestaurant().getId());
	}
	
	@Test
	void shouldReturnBadRequestWhenNameIsBlank() throws Exception{
		Restaurant restaurant = createRestaurant();

		String requestBody = """
				{
				"name":" ",
				"price": 38.80,
				"restaurantId": %d
				}
				""".formatted(restaurant.getId());
		
		mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(requestBody))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnBadRequestWhenPriceIsNull() throws Exception{
		Restaurant restaurant = createRestaurant();

		String requestBody = """
				{
				"name":"Pizza 4Queijos",
				"price": null,
				"restaurantId": %d
				}
				""".formatted(restaurant.getId());
		
		mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(requestBody))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnNotFoundWhenRestaurantDoesNotExist() throws Exception{	
		String requestBody = """
				{
				"name":"Pizza 4Queijos",
				"price": 38.80,
				"restaurantId":999
				}
				""";

		mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isNotFound());		
	}
	
	@Test
	void shouldUpdateProduct() throws Exception {
		Restaurant restaurant = createRestaurant();
		
		Product product = createProduct(restaurant);

		String requestBody = """
				{
				"name":"Pizza 4Queijos",
				"price": 38.80,
				"restaurantId": %d
				}
				""".formatted(restaurant.getId());

		mockMvc.perform(put("/api/products/" + product.getId()).contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("Pizza 4Queijos"))
				.andExpect(jsonPath("$.price").value(38.80))
				.andExpect(jsonPath("$.restaurantId").value(restaurant.getId()))
				.andExpect(jsonPath("$.restaurantName").value("Pizzaria do Marcelo"));
		
		Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();

		assertEquals("Pizza 4Queijos", updatedProduct.getName());
		assertEquals(0, new BigDecimal("38.80").compareTo(updatedProduct.getPrice()));
		assertEquals(restaurant.getId(), updatedProduct.getRestaurant().getId());
	}
	
	@Test
	void shouldReturnNotFoundWhenUpdatingNonExistingProduct() throws Exception {
		Restaurant restaurant = createRestaurant();
		
		String requestBody = """
				{
				"name":"Pizza 4Queijos",
				"price": 38.80,
				"restaurantId": %d
				}
				""".formatted(restaurant.getId());

		mockMvc.perform(put("/api/products/" + 999).contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnBadRequestWhenUpdatingProductWithBlankName() throws Exception{
		Restaurant restaurant = createRestaurant();
		
		Product product = createProduct(restaurant);

		String requestBody = """
				{
				"name":" ",
				"price": 38.80,
				"restaurantId": %d
				}
				""".formatted(restaurant.getId());

		mockMvc.perform(put("/api/products/" + product.getId()).contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnBadRequestWhenUpdatingProductWithNullPrice() throws Exception {
		Restaurant restaurant = createRestaurant();
		
		Product product = createProduct(restaurant);

		String requestBody = """
				{
				"name":"Pizza 4Queijos ",
				"price": null,
				"restaurantId": %d
				}
				""".formatted(restaurant.getId());

		mockMvc.perform(put("/api/products/" + product.getId()).contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnNotFoundWhenUpdatingProductWithNonExistingRestaurant() throws Exception {
		Restaurant restaurant = createRestaurant();
		
		Product product = createProduct(restaurant);
		
		String requestBody = """
				{
				"name":"Pizza 4Queijos",
				"price": 38.80,
				"restaurantId": 999
				}
				""";

		mockMvc.perform(put("/api/products/" + product.getId()).contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldDeleteProduct() throws Exception {
		Restaurant restaurant = createRestaurant();
		
		Product product = createProduct(restaurant);

		mockMvc.perform(delete("/api/products/" + product.getId())).andExpect(status().isNoContent());
		
		assertTrue(productRepository.findById(product.getId()).isEmpty());
	}
	
	@Test
	void shouldReturnNotFoundWhenDeletingNonExistingProduct() throws Exception {
		mockMvc.perform(delete("/api/products/" + 999)).andExpect(status().isNotFound());
	}
	
	private Restaurant createRestaurant() {
		Restaurant restaurant = restaurantRepository
				.save(new Restaurant("Pizzaria do Marcelo", "Pizza", BigDecimal.valueOf(5.0)));
		return restaurant;
	}
	
	private Product createProduct(Restaurant restaurant) {
		Product product = productRepository.save(new Product("Pizza 4Queijos", BigDecimal.valueOf(38.80), restaurant));
		return product;
	}
}