package com.delivery_management_api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Product creation request")
public class CreateProductRequest {
	
	@Schema(description = "Product name", example = "Hambúrguer")
	@NotBlank
	private String name;
	
	@Schema(description = "Product price", example = "29.90")
	@NotNull
	@DecimalMin("0.0")
	private BigDecimal price;
	
	@Schema(description = "Restaurant identifier", example = "1")
	@NotNull
	private Long restaurantId;
	
	public CreateProductRequest() {
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