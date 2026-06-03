package com.delivery_management_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class OrderItemResponse {
	
	@Schema(description = "Product identifier",example = "1")
	private Long productId;
	
	@Schema(description = "Product name",example = "Hambúrguer")
	private String productName;
	
	@Schema(description = "Quantity",example = "5")
	private Integer quantity;
	
	public OrderItemResponse() {
	}
	
	public OrderItemResponse(Long productId, String productName, Integer quantity) {
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public Integer getQuantity() {
		return quantity;
	}
}