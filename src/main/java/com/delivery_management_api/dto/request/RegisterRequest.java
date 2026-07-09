package com.delivery_management_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "User registration request")
public class RegisterRequest {

	@Schema(description = "User full name", example = "João da Silva")
	@NotBlank
	private String name;

	@Schema(description = "User email address", example = "joao@email.com")
	@NotBlank
	@Email
	private String email;

	@Schema(description = "User password (minimum 8 characters)", example = "senha123")
	@NotBlank
	@Size(min = 8)
	private String password;

	public RegisterRequest() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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