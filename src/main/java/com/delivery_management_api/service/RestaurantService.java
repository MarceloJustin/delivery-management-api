package com.delivery_management_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delivery_management_api.dto.CreateRestaurantRequest;
import com.delivery_management_api.dto.RestaurantResponse;
import com.delivery_management_api.dto.UpdateRestaurantRequest;
import com.delivery_management_api.entity.Restaurant;
import com.delivery_management_api.exception.RestaurantNotFoundException;
import com.delivery_management_api.repository.RestaurantRepository;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
		Restaurant restaurant = new Restaurant(request.getName(), request.getCategory(), request.getDeliveryFee());
		Restaurant savedRestaurant = restaurantRepository.save(restaurant);
		return new RestaurantResponse(savedRestaurant.getId(), savedRestaurant.getName(), savedRestaurant.getCategory(),
				savedRestaurant.getDeliveryFee());
	}

	public List<RestaurantResponse> findAllRestaurants() {
		return restaurantRepository.findAll().stream().map(restaurant -> new RestaurantResponse(restaurant.getId(),
				restaurant.getName(), restaurant.getCategory(), restaurant.getDeliveryFee())).toList();
	}

	public RestaurantResponse findRestaurantById(Long id) {
		Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
		return new RestaurantResponse(restaurant.getId(), restaurant.getName(), restaurant.getCategory(),
				restaurant.getDeliveryFee());
	}

	public RestaurantResponse updateRestaurant(Long id, UpdateRestaurantRequest request) {
		Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
		restaurant.setName(request.getName());
		restaurant.setCategory(request.getCategory());
		restaurant.setDeliveryFee(request.getDeliveryFee());
		Restaurant UpdateRestaurant = restaurantRepository.save(restaurant);
		return new RestaurantResponse(UpdateRestaurant.getId(), UpdateRestaurant.getName(),
				UpdateRestaurant.getCategory(), UpdateRestaurant.getDeliveryFee());
	}

	public void deleteRestaurant(Long id) {
		restaurantRepository.deleteById(id);
	}
}