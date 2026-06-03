package com.delivery_management_api.exception;

public class InvalidOrderStatusException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InvalidOrderStatusException(String currentStatus, String newStatus) {
		super("Cannot change order status from "+ currentStatus + " to " + newStatus);
	}
}
