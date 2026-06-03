package com.delivery_management_api.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_management_api.dto.CreateCustomerRequest;
import com.delivery_management_api.dto.CustomerResponse;
import com.delivery_management_api.dto.ErrorResponse;
import com.delivery_management_api.dto.UpdateCustomerRequest;
import com.delivery_management_api.dto.ValidationErrorResponse;
import com.delivery_management_api.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Operations related to customer management")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PostMapping
	@Operation(summary = "Create customer", description = "Creates a new customer")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Customer created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))) })
	public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
	}

	@GetMapping
	@Operation(summary = "List customers", description = "Returns all registered customers")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Customers found") })
	public ResponseEntity<Page<CustomerResponse>> findAllCustomers(Pageable pageable) {
		return ResponseEntity.ok(customerService.findAllCustomers(pageable));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Find customer by ID", description = "Returns a customer by its ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Customer found"),
			@ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<CustomerResponse> findCustomerById(@PathVariable Long id) {
		return ResponseEntity.ok(customerService.findCustomerById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update customer", description = "Updates an existing customer")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Customer updated"),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @Valid @RequestBody UpdateCustomerRequest request) {
		return ResponseEntity.ok(customerService.updateCustomer(id, request));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete customer", description = "Deletes a customer by ID")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Customer deleted"),
			@ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
		customerService.deleteCustomer(id);
		return ResponseEntity.noContent().build();
	}
}