package com.delivery_management_api.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CreateOrderRequest {
	
	@NotNull
	private Long customerId;
	
	@NotNull
	private Long restaurantId;
	
	@NotEmpty
	private List<CreateOrderItemRequest> items;
	
	public CreateOrderRequest() {
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public List<CreateOrderItemRequest> getItems() {
		return items;
	}

	public void setItems(List<CreateOrderItemRequest> items) {
		this.items = items;
	}
}