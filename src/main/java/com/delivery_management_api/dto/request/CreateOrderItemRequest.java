package com.delivery_management_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Create order items")
public class CreateOrderItemRequest {
	
	@Schema(description = "Product identifier", example = "1")
	@NotNull
	private Long productId;
	
	@Schema(description = "Product quantity", example = "5")
	@NotNull
	@Min(1)
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
