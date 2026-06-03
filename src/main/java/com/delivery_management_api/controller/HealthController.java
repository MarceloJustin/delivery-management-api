package com.delivery_management_api.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Health Check", description = "Application health monitoring")
public class HealthController {

	@GetMapping("/api/health")
	@Operation(summary = "Application health check", description = "Returns the current application status")
	@ApiResponses({@ApiResponse(responseCode = "200",description = "Application is running")})
	public Map<String, String> health(){
		return Map.of(
				"status", "UP",
				"application", "Delivery Management API"
				);
	}
}
