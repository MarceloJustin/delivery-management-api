package com.delivery_management_api.exception;

public class RestaurantNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public RestaurantNotFoundException(Long id) {
		super("Restaurant with id " + id + " not found" );
	}

}
