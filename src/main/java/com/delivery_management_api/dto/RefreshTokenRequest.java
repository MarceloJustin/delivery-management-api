package com.delivery_management_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Refresh token request")
public class RefreshTokenRequest {

	@Schema(description = "Refresh token issued during login", example = "b6f1c9e2-3a4d-4e2b-9c1a-5f6e7d8c9b0a")
	@NotBlank
	private String refreshToken;

	public RefreshTokenRequest() {
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}