package com.delivery_management_api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.delivery_management_api.entity.Product;
import com.delivery_management_api.entity.Restaurant;
import com.delivery_management_api.repository.ProductRepository;
import com.delivery_management_api.repository.RestaurantRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
		Restaurant restaurant = restaurantRepository
				.save(new Restaurant("Pizzaria do Marcelo", "Pizza", BigDecimal.valueOf(5.0)));
		Product product = productRepository.save(new Product("Pizza Calabresa", BigDecimal.valueOf(39.90), restaurant));

		mockMvc.perform(get("/api/products/" + product.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(product.getId()))
				.andExpect(jsonPath("$.name").value("Pizza Calabresa")).andExpect(jsonPath("$.price").value(39.90))
				.andExpect(jsonPath("$.restaurantId").value(restaurant.getId()))
				.andExpect(jsonPath("$.restaurantName").value("Pizzaria do Marcelo"));
	}

	@Test
	void shouldCreateProduct() throws Exception {
		Restaurant restaurant = restaurantRepository
				.save(new Restaurant("Pizzaria do Marcelo", "Pizza", BigDecimal.valueOf(5.0)));

		String requestBody = """
				{
				"name":"Pizza Calabresa",
				"price": 39.90,
				"restaurantId": %d
				}
				""".formatted(restaurant.getId());

		mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("Pizza Calabresa"))
				.andExpect(jsonPath("$.price").value(39.90))
				.andExpect(jsonPath("$.restaurantId").value(restaurant.getId()))
				.andExpect(jsonPath("$.restaurantName").value("Pizzaria do Marcelo"));
	}
}
