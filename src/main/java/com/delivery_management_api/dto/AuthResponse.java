package com.delivery_management_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response with JWT token")
public class AuthResponse {

	@Schema(description = "JWT token", example = "eyJhbGciOiJIUzI1NiJ9...")
	private String token;

	@Schema(description = "Token type", example = "Bearer")
	private String type;

	@Schema(description = "User full name", example = "João da Silva")
	private String name;

	@Schema(description = "User email address", example = "joao@email.com")
	private String email;

	@Schema(description = "User role", example = "CUSTOMER")
	private String role;

	public AuthResponse() {
	}

	public AuthResponse(String token, String name, String email, String role) {
		this.token = token;
		this.type = "Bearer";
		this.name = name;
		this.email = email;
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
	}

}