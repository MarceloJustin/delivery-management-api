package com.delivery_management_api.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.transaction.annotation.Transactional;

import com.delivery_management_api.entity.Restaurant;
import com.delivery_management_api.repository.RestaurantRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class RestaurantControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Test
	void shouldCreateRestaurant() throws Exception {
		String requestBody = """
				{
				  "name": "Burger King",
				  "category": "Fast Food",
				  "deliveryFee": 9.9
				}""";

		mockMvc.perform(post("/api/restaurants").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.name").value("Burger King"))
				.andExpect(jsonPath("$.category").value("Fast Food")).andExpect(jsonPath("$.deliveryFee").value(9.9));

		Restaurant restaurantCreated = restaurantRepository.findAll().getFirst();

		assertNotNull(restaurantCreated.getId());
		assertEquals("Burger King", restaurantCreated.getName());
		assertEquals("Fast Food", restaurantCreated.getCategory());
		assertEquals(0, new BigDecimal("9.9").compareTo(restaurantCreated.getDeliveryFee()));
	}

	@Test
	void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
		String requestBody = """
				{
				  "name": " ",
				  "category": "Fast Food",
				  "deliveryFee": 9.9
				}
				""";

		mockMvc.perform(post("/api/restaurants").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnRestaurantList() throws Exception {
		createRestaurant();
		
		restaurantRepository.save(new Restaurant("Pizzaria Itália","Pizza",BigDecimal.valueOf(5.0)));

		mockMvc.perform(get("/api/restaurants")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(2)).andExpect(jsonPath("$.totalElements").value(2));
	}

	@Test
	void shouldReturnRestaurantWhenExists() throws Exception {
		Restaurant restaurant = createRestaurant();

		mockMvc.perform(get("/api/restaurants/" + restaurant.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(restaurant.getId()))
				.andExpect(jsonPath("$.name").value("Burger King")).andExpect(jsonPath("$.category").value("Fast Food"))
				.andExpect(jsonPath("$.deliveryFee").value(9.9));
	}

	@Test
	void shouldReturnNotFoundWhenRestaurantDoesNotExist() throws Exception {
		mockMvc.perform(get("/api/restaurants/999")).andExpect(status().isNotFound());
	}

	@Test
	void shouldReturnRestaurantsMatchingName() throws Exception {
		createRestaurant();
		
		restaurantRepository.save(new Restaurant("Pizzaria Itália","Pizza",BigDecimal.valueOf(5.0)));

		mockMvc.perform(get("/api/restaurants/search/Burger")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	void shouldUpdateRestaurant() throws Exception {
		Restaurant restaurant = createRestaurant();

		String requestBody = """
				{
				  "name": "Burger King Premium",
				  "category": "Hamburgueria",
				  "deliveryFee": 12.5
				}
				""";

		mockMvc.perform(put("/api/restaurants/" + restaurant.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(requestBody)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Burger King Premium"))
				.andExpect(jsonPath("$.category").value("Hamburgueria"))
				.andExpect(jsonPath("$.deliveryFee").value(12.5));

		Restaurant updated = restaurantRepository.findById(restaurant.getId()).orElseThrow();

		assertEquals("Hamburgueria", updated.getCategory());
		assertEquals(0, BigDecimal.valueOf(12.5).compareTo(updated.getDeliveryFee()));
		assertEquals("Burger King Premium", updated.getName());
	}

	@Test
	void shouldReturnNotFoundWhenUpdatingNonExistingRestaurant() throws Exception {
		String requestBody = """
				{
				  "name": "Burger King Premium",
				  "category": "Hamburgueria",
				  "deliveryFee": 12.5
				}
				""";

		mockMvc.perform(put("/api/restaurants/999").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldDeleteRestaurant() throws Exception {
		Restaurant restaurant = createRestaurant();

		mockMvc.perform(delete("/api/restaurants/" + restaurant.getId())).andExpect(status().isNoContent());

		assertFalse(restaurantRepository.findById(restaurant.getId()).isPresent());
	}

	@Test
	void shouldReturnNotFoundWhenDeletingNonExistingRestaurant() throws Exception {
		mockMvc.perform(delete("/api/restaurants/999")).andExpect(status().isNotFound());
	}

	private Restaurant createRestaurant() {
		return restaurantRepository.save(new Restaurant("Burger King", "Fast Food", BigDecimal.valueOf(9.9)));
	}
}