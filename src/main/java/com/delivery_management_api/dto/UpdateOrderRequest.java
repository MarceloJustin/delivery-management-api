package com.delivery_management_api.dto;

import java.util.List;

public class UpdateOrderRequest {
	
	private Long restaurantId;
	private Long customerId;
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