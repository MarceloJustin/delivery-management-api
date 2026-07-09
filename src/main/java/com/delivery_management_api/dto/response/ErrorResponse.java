package com.delivery_management_api.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API error response")
public class ErrorResponse {
	
	@Schema(description = "Timestamp when the error occurred", example = "2026-06-02T20:15:30")
	private LocalDateTime timestamp;
	
	@Schema(description = "HTTP status code", example = "404")
	private int status;
	
	@Schema(description = "HTTP error name", example = "Not Found")
	private String error;
	
	@Schema(description = "Detailed error message", example = "Customer with id 99 not found")
	private String message;
	
	public ErrorResponse(LocalDateTime timestamp, int status, String error, String message) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
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
}