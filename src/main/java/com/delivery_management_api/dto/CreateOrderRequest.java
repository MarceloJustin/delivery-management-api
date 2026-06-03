package com.delivery_management_api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Order creation request")
public class CreateOrderRequest {
	
	@Schema(description = "Customer identifier", example = "1")
	@NotNull
	private Long customerId;
	
	@Schema(description = "Restaurant identifier", example = "1")
	@NotNull
	private Long restaurantId;
	
	@Schema(description = "List of order items")
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