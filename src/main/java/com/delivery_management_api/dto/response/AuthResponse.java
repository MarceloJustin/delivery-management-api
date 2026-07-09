package com.delivery_management_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response with JWT token")
public class AuthResponse {

	@Schema(description = "JWT token", example = "eyJhbGciOiJIUzI1NiJ9...")
	private String token;

	@Schema(description = "Token type", example = "Bearer")
	private String type;

	@Schema(description = "Refresh token used to obtain a new access token", example = "b6f1c9e2-3a4d-4e2b-9c1a-5f6e7d8c9b0a")
	private String refreshToken;

	@Schema(description = "User full name", example = "João da Silva")
	private String name;

	@Schema(description = "User email address", example = "joao@email.com")
	private String email;

	@Schema(description = "User role", example = "CUSTOMER")
	private String role;

	public AuthResponse() {
	}

	public AuthResponse(String token, String name, String email, String role) {
		this(token, null, name, email, role);
	}

	public AuthResponse(String token, String refreshToken, String name, String email, String role) {
		this.token = token;
		this.type = "Bearer";
		this.refreshToken = refreshToken;
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

	public String getRefreshToken() {
		return refreshToken;
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