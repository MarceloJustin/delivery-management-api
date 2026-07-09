package com.delivery_management_api.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateOrderRequest {
	
	@Schema(description = "Restaurant identifier",example = "1")
	private Long restaurantId;
	
	@Schema(description = "Customer identifier",example = "1")
	private Long customerId;
	
	@Schema(description = "List of order items")
	private List<UpdateOrderItemRequest> items;
	
	public UpdateOrderRequest() {
	}
	
	public UpdateOrderRequest(Long restaurantId, Long custumerId, List<UpdateOrderItemRequest> items) {
		
		this.restaurantId = restaurantId;
		this.customerId = custumerId;
		this.items = items;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public List<UpdateOrderItemRequest> getItems() {
		return items;
	}

	public void setItems(List<UpdateOrderItemRequest> items) {
		this.items = items;
	}
}