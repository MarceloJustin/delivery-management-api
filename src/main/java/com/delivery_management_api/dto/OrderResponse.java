package com.delivery_management_api.dto;

import java.math.BigDecimal;
import java.util.List;

import com.delivery_management_api.enums.OrderStatus;

public class OrderResponse {
	
	private Long id;
	private Long customerId;
	private String customerName;
	private Long restaurantId;
	private String restaurantName;
	private BigDecimal totalAmount;
	private OrderStatus status;
	private List<OrderItemResponse> items;
	
	public OrderResponse() {
	}
	
	public OrderResponse(Long id, Long customerId, String customerName, Long restaurantId, String restaurantName,
			BigDecimal totalAmount, OrderStatus status, List<OrderItemResponse> items) {
		this.id = id;
		this.customerId = customerId;
		this.customerName = customerName;
		this.restaurantId = restaurantId;
		this.restaurantName = restaurantName;
		this.totalAmount = totalAmount;
		this.status = status;
		this.items = items;
	}

	public Long getId() {
		return id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public List<OrderItemResponse> getItems() {
		return items;
	}
}