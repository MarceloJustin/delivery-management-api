package com.delivery_management_api.dto;

import java.math.BigDecimal;

public class ProductResponse {
	
	private Long id;
	private String name;
	private BigDecimal price;
	private Long restaurantId;
	private String restaurantName;
	
	public ProductResponse(Long id, String name, BigDecimal price, Long restaurantId, String restaurantName) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.restaurantId = restaurantId;
		this.restaurantName = restaurantName;
	}
	
	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public Long getId() {
		return id;
	}
}
