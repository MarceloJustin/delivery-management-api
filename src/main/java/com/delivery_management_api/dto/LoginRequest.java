package com.delivery_management_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User login request")
public class LoginRequest {

	@Schema(description = "User email address", example = "joao@email.com")
	@NotBlank
	@Email
	private String email;

	@Schema(description = "User password", example = "senha123")
	@NotBlank
	private String password;

	public LoginRequest() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}