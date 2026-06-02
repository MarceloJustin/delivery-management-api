package com.delivery_management_api.exception;

public class OrderCancellationNotAllowedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public OrderCancellationNotAllowedException() {
		super("Only orders with status CREATED can be cancelled");
	}
}
