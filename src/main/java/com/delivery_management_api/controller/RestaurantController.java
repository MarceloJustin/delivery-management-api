package com.delivery_management_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_management_api.dto.CreateRestaurantRequest;
import com.delivery_management_api.dto.RestaurantResponse;
import com.delivery_management_api.dto.UpdateRestaurantRequest;
import com.delivery_management_api.service.RestaurantService;

@RestController
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;

	@PostMapping("/api/restaurants")
	public RestaurantResponse creatRestaurant(@RequestBody CreateRestaurantRequest request) {
		return restaurantService.createRestaurant(request);
	}

	@GetMapping("/api/restaurants")
	public List<RestaurantResponse> findAllRestaurant() {
		return restaurantService.findAllRestaurants();
	}

	@GetMapping("/api/restaurants/{id}")
	public RestaurantResponse findRestaurantById(@PathVariable Long id) {
		return restaurantService.findRestaurantById(id);
	}

	@PutMapping("/api/restaurants/{id}")
	public RestaurantResponse updateRestaurant(@PathVariable Long id, @RequestBody UpdateRestaurantRequest request) {
		return restaurantService.updateRestaurant(id, request);
	}

	@DeleteMapping("/api/restaurants/{id}")
	public void deleteRestaurant(@PathVariable Long id) {
		restaurantService.deleteRestaurant(id);
	}
}