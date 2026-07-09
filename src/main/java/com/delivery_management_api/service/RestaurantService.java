package com.delivery_management_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.delivery_management_api.dto.request.CreateRestaurantRequest;
import com.delivery_management_api.dto.response.RestaurantResponse;
import com.delivery_management_api.dto.request.UpdateRestaurantRequest;
import com.delivery_management_api.entity.Restaurant;
import com.delivery_management_api.exception.RestaurantNotFoundException;
import com.delivery_management_api.mapper.RestaurantMapper;
import com.delivery_management_api.repository.RestaurantRepository;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private RestaurantMapper restaurantMapper;

	public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
		Restaurant restaurant = new Restaurant(request.getName(), request.getCategory(), request.getDeliveryFee());
		Restaurant savedRestaurant = restaurantRepository.save(restaurant);
		return restaurantMapper.toResponse(savedRestaurant);
	}

	public Page<RestaurantResponse> findAllRestaurants(Pageable pageable) {
		return restaurantRepository.findAll(pageable).map(restaurantMapper::toResponse);
	}

	public RestaurantResponse findRestaurantById(Long id) {
		Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
		return restaurantMapper.toResponse(restaurant);
	}

	public List<RestaurantResponse> findRestaurantsByName(String name){
		return restaurantRepository.findByNameContainingIgnoreCase(name).stream()
				.map(restaurantMapper::toResponse).toList();
	}

	public RestaurantResponse updateRestaurant(Long id, UpdateRestaurantRequest request) {
		Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
		restaurant.setName(request.getName());
		restaurant.setCategory(request.getCategory());
		restaurant.setDeliveryFee(request.getDeliveryFee());
		Restaurant updateRestaurant = restaurantRepository.save(restaurant);
		return restaurantMapper.toResponse(updateRestaurant);
	}

	public void deleteRestaurant(Long id) {
		restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
		restaurantRepository.deleteById(id);
	}
}