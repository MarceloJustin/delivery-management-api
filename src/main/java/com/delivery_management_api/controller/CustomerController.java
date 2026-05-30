package com.delivery_management_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_management_api.dto.CreateCustomerRequest;
import com.delivery_management_api.dto.CustomerResponse;
import com.delivery_management_api.service.CustomerService;



@RestController
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;

	@PostMapping("/api/customers")
	public CustomerResponse createCustomer(@RequestBody CreateCustomerRequest request) {
		return customerService.createCustomer(request);
	}

	@GetMapping("/api/customers")
	public List<CustomerResponse> findAllCustomers() {
		return customerService.findAllCustomers();
	}
}
