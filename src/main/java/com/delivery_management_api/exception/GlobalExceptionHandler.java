package com.delivery_management_api.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.delivery_management_api.dto.ErrorResponse;
import com.delivery_management_api.dto.ValidationErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler({
		CustomerNotFoundException.class,
		ProductNotFoundException.class,
		RestaurantNotFoundException.class,
		OrderNotFoundException.class
	})
	public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
		
		ErrorResponse error = new ErrorResponse(LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.getReasonPhrase(),
				ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {

	    Map<String, String> validationErrors = new HashMap<>();

	    ex.getBindingResult().getFieldErrors().forEach(error -> validationErrors.put(
	            error.getField(),
	            error.getDefaultMessage()));

	    ValidationErrorResponse response = new ValidationErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            HttpStatus.BAD_REQUEST.getReasonPhrase(),
	            "Validation failed",
	            validationErrors);

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
	
	
	@ExceptionHandler(InvalidOrderStatusException.class)
	public ResponseEntity<ErrorResponse> handleInvalidStatus(InvalidOrderStatusException ex){

		ErrorResponse error = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.CONFLICT.value(),
				HttpStatus.CONFLICT.getReasonPhrase(),
				ex.getMessage());

		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(error);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex) {

		ErrorResponse error = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.getReasonPhrase(),
				ex.getMessage());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {

		ErrorResponse error = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.FORBIDDEN.value(),
				HttpStatus.FORBIDDEN.getReasonPhrase(),
				"You do not have permission to access this resource");

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {

		ErrorResponse error = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.CONFLICT.value(),
				HttpStatus.CONFLICT.getReasonPhrase(),
				ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}
}