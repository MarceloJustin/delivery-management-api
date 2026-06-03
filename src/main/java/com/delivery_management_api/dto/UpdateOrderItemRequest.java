package com.delivery_management_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Order item update request")
public class UpdateOrderItemRequest {
	
	@Schema(description = "Product identifier",example = "1")
	@NotNull
	private Long productId;
	
	@Schema(description = "Product quantity",example = "5")
	@NotNull
	private Integer quantity;

	public UpdateOrderItemRequest() {
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