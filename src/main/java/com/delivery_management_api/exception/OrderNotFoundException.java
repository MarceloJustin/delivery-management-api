package com.delivery_management_api.exception;

public class OrderNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public OrderNotFoundException(Long id) {
		super("Order with id " + id + " not found" );
	}
}
