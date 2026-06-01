package com.delivery_management_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateProductRequest {
	
	@NotBlank
	private String name;
	
	@NotNull
	@DecimalMin("0.0")
	private BigDecimal price;
	
	@NotNull
	private Long restaurantId;
	
	public UpdateProductRequest() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getRestaurantId() {
	    return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
	    this.restaurantId = restaurantId;
	}
}