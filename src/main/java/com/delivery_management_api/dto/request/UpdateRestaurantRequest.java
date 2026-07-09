package com.delivery_management_api.dto.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Restaurant update request")
public class UpdateRestaurantRequest {
	
	@Schema(description = "Restaurant name", example = "Burger King")
	@NotBlank
	private String name;
	
	@Schema(description = "Restaurant category", example = "Fast Food")
	@NotBlank
	private String category;
	
	@Schema(description = "Delivery fee", example = "9.90")
	@NotNull
	@DecimalMin("0.0")
	private BigDecimal deliveryFee;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}
	
	public void setDeliveryFee(BigDecimal deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
}