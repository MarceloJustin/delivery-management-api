package com.delivery_management_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderCancellationNotAllowedException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public OrderCancellationNotAllowedException() {
			super("Only orders with status CREATED can be cancelled");
		}
	}
