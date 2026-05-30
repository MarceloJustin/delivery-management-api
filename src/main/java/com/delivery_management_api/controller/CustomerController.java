package com.delivery_management_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_management_api.dto.CreateCustomerRequest;
import com.delivery_management_api.dto.CustomerResponse;
import com.delivery_management_api.dto.UpdateCustomerRequest;
import com.delivery_management_api.repository.CustomerRepository;
import com.delivery_management_api.service.CustomerService;


@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;
	
	@Autowired
	private CustomerService customerService;

    CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

	@PostMapping("/api/customers")
	public CustomerResponse createCustomer(@RequestBody CreateCustomerRequest request) {
		return customerService.createCustomer(request);
	}

	@GetMapping("/api/customers")
	public List<CustomerResponse> findAllCustomers() {
		return customerService.findAllCustomers();
	}
	
	@GetMapping("/api/customers/{id}")
	public CustomerResponse findCustomerById(@PathVariable Long id) {
		return customerService.findCustomerById(id);
	}
	
	@PutMapping("/api/customer/{id}")
	public CustomerResponse UpdateCustomerById(@PathVariable Long id, @RequestBody UpdateCustomerRequest request) {
		return customerService.updateCustomer(id, request);
	}
	
	@DeleteMapping("/api/customer/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		customerService.deleteCustomer(id);
	}
}

























