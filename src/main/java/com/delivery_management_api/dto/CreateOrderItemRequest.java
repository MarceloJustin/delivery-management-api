package com.delivery_management_api.dto;

import jakarta.validation.constraints.NotNull;

public class CreateOrderItemRequest {
	
	@NotNull
	private Long productId;
	
	@NotNull
	private Integer quantity;

	public CreateOrderItemRequest() {
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
