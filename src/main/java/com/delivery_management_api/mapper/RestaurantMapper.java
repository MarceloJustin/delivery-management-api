package com.delivery_management_api.mapper;

import org.springframework.stereotype.Component;

import com.delivery_management_api.dto.response.RestaurantResponse;
import com.delivery_management_api.entity.Restaurant;

@Component
public class RestaurantMapper {

	public RestaurantResponse toResponse(Restaurant restaurant) {
		return new RestaurantResponse(restaurant.getId(), restaurant.getName(), restaurant.getCategory(),
				restaurant.getDeliveryFee());
	}

}