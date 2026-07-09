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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.delivery_management_api.entity.Product;
import com.delivery_management_api.entity.Restaurant;
import com.delivery_management_api.repository.ProductRepository;
import com.delivery_management_api.repository.RestaurantRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class OrderAuthorizationIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private ProductRepository productRepository;

	@Test
	void customerShouldOnlySeeOwnOrders() throws Exception {
		Restaurant restaurant = restaurantRepository.save(new Restaurant("Burger King", "Fast Food", BigDecimal.valueOf(5.0)));
		Product product = productRepository.save(new Product("Burger", BigDecimal.valueOf(20.0), restaurant));

		String tokenA = registerAndGetToken("joaoA@email.com");
		String tokenB = registerAndGetToken("joaoB@email.com");

		createOrder(tokenA, restaurant.getId(), product.getId());
		createOrder(tokenB, restaurant.getId(), product.getId());

		mockMvc.perform(get("/api/orders").header("Authorization", "Bearer " + tokenA))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(1))
				.andExpect(jsonPath("$.totalElements").value(1));
	}

	private String registerAndGetToken(String email) throws Exception {
		String body = """
				{
				  "name": "Teste",
				  "email": "%s",
				  "password": "senha123"
				}
				""".formatted(email);

		MvcResult result = mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isCreated())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		return responseBody.split("\"token\":\"")[1].split("\"")[0];
	}

	private void createOrder(String token, Long restaurantId, Long productId) throws Exception {
		String body = """
				{
				  "customerId": 1,
				  "restaurantId": %d,
				  "items": [
				    { "productId": %d, "quantity": 1 }
				  ]
				}
				""".formatted(restaurantId, productId);

		mockMvc.perform(post("/api/orders")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isCreated());
	}
}