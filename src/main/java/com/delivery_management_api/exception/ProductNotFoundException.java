package com.delivery_management_api.exception;

public class ProductNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ProductNotFoundException(Long id) {
		super("Product with id " + id + " not found" );
	}
}
