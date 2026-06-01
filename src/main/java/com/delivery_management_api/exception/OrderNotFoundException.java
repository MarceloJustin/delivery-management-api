package com.delivery_management_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public OrderNotFoundException(Long id) {
		super("Order with id " + id + " not found" );
	}
}
