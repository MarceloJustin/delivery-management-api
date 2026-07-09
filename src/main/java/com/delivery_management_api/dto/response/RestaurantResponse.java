package com.delivery_management_api.dto.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Restaurant response")
public class RestaurantResponse {
	
	@Schema(description = "Restaurant identifier", example = "1")
	private Long id;
	
	@Schema(description = "Restaurant name", example = "Burger King") 
	private String name;
	
	@Schema(description = "Restaurant category", example = "Fast Food")
	private String category;
	
	@Schema(description = "Delivery fee", example = "8.50")
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
