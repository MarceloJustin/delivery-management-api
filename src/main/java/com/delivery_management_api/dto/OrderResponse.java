package com.delivery_management_api.dto;

import java.math.BigDecimal;
import java.util.List;

import com.delivery_management_api.enums.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Order response data")
public class OrderResponse {
	
	@Schema(description = "Order identifier", example = "1")
	private Long id;
	
	@Schema(description = "Customer identifier", example = "1")
	private Long customerId;
	
	@Schema(description = "Customer name", example = "João da Silva")
	private String customerName;
	
	@Schema(description = "Restaurant identifier", example = "1")
	private Long restaurantId;
	
	@Schema(description = "Restaurant name", example = "Burger King")
	private String restaurantName;
	
	@Schema(description = "Delivery fee", example = "9.90")
	private BigDecimal deliveryFee;
	
	@Schema(description = "Total order amount", example = "119.90")
	private BigDecimal totalAmount;
	
	@Schema(description = "Current order status", example = "CREATED")
	private OrderStatus status;
	
	@Schema(description = "Order items")
	private List<OrderItemResponse> items;
	
	public OrderResponse() {
	}
	
	public OrderResponse(Long id, Long customerId, String customerName, Long restaurantId, String restaurantName,
			BigDecimal deliveryFee, BigDecimal totalAmount, OrderStatus status, List<OrderItemResponse> items) {
		this.id = id;
		this.customerId = customerId;
		this.customerName = customerName;
		this.restaurantId = restaurantId;
		this.restaurantName = restaurantName;
		this.deliveryFee = deliveryFee;
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

	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}
}