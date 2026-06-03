package com.delivery_management_api.dto;

import java.time.LocalDateTime;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Validation error response")
public class ValidationErrorResponse {
	
	@Schema(description = "Timestamp when the error occurred", example = "2026-06-02T20:15:30")
	private LocalDateTime timestamp;
	
	@Schema(description = "HTTP status code", example = "400")
	private int status;
	
	@Schema(description = "HTTP error name", example = "Bad Request")
	private String error;
	
	@Schema(description = "Validation error message", example = "Validation failed")
	private String message; 
	
	@Schema(description = "List of validation errors by field")
	private Map<String, String> validationErrors;
	
	public ValidationErrorResponse(LocalDateTime timestamp, int status, String error, String message,
			Map<String, String> validationErrors) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.validationErrors = validationErrors;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public int getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public Map<String, String> getValidationErrors() {
		return validationErrors;
	}
}