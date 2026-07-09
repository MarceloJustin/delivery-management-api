package com.delivery_management_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Customer update request")
public class UpdateCustomerRequest {
	
	@Schema(description = "Customer full name", example = "João da Silva")
	@NotBlank
	private String name;
	
	@Schema(description = "Customer email address", example = "joaodasilva@email.com")
	@Email
	private String email;

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
}
