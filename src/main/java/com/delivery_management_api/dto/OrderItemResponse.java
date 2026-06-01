package com.delivery_management_api.dto;

public class OrderItemResponse {
	
	private Long productId;
	private String productName;
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