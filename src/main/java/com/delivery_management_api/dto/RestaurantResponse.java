package com.delivery_management_api.dto;

import java.math.BigDecimal;

public class RestaurantResponse {
	
	private Long id;
	private String name;
	private String category;
	private BigDecimal deliveryFee;
	
	public RestaurantResponse(Long id, String name, String category, BigDecimal deliveryFee) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.deliveryFee = deliveryFee;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}
}
