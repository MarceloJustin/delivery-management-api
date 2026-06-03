package com.delivery_management_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Customer response data")
public class CustomerResponse {
	
	@Schema(description = "Customer identifier", example = "1")
	private Long id;
	
	@Schema(description = "Customer full name", example = "Marcelo Justin")
	private String name;
	
	@Schema(description = "Customer email address", example = "marcelo@email.com")
	private String email;
	
	public CustomerResponse() {
	}
	
	public CustomerResponse(Long id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
}
