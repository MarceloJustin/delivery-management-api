package com.delivery_management_api.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.delivery_management_api.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler({
		CustomerNotFoundException.class,
		ProductNotFoundException.class,
		RestaurantNotFoundException.class,
		OrderNotFoundException.class
	})
	
	public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
		
		ErrorResponse error = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.getReasonPhrase(),
				ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(OrderCancellationNotAllowedException.class)
	
	public ResponseEntity<ErrorResponse> handleOrderCancellation(OrderCancellationNotAllowedException ex) {
		
		ErrorResponse error = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.CONFLICT.value(),
				HttpStatus.CONFLICT.getReasonPhrase(),
				ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}
}