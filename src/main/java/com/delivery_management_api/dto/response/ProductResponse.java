package com.delivery_management_api.dto.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product response")
public class ProductResponse {
	
	@Schema(description = "Product identifier",example = "1")
	private Long id;
	
	@Schema(description = "Product name", example = "Hambúrguer")
	private String name;
	
	@Schema(description = "Product price", example = "29.90")
	private BigDecimal price;
	
	@Schema(description = "Restaurant identifier", example = "1")
	private Long restaurantId;
	
	@Schema(description = "Restaurant name", example = "Burger King")
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
