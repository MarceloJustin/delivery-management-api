package com.delivery_management_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delivery_management_api.dto.CreateCustomerRequest;
import com.delivery_management_api.dto.CustomerResponse;
import com.delivery_management_api.dto.UpdateCustomerRequest;
import com.delivery_management_api.entity.Customer;
import com.delivery_management_api.exception.CustomerNotFoundException;
import com.delivery_management_api.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public CustomerResponse createCustomer(CreateCustomerRequest request) {
		Customer customer = new Customer(request.getName(), request.getEmail());
		Customer savedCustomer = customerRepository.save(customer);
		return new CustomerResponse(savedCustomer.getId(), savedCustomer.getName(), savedCustomer.getEmail());
	}

	public List<CustomerResponse> findAllCustomers() {
		return customerRepository.findAll().stream()
				.map(customer -> new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail()))
				.toList();
	}

	public CustomerResponse findCustomerById(Long id) {
		Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
		return new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail());
	}

	public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
		Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
		customer.setName(request.getName());
		customer.setEmail(request.getEmail());
		Customer updatedCustomer = customerRepository.save(customer);
		return new CustomerResponse(updatedCustomer.getId(), updatedCustomer.getName(), updatedCustomer.getEmail());
	}

	public void deleteCustomer(Long id) {
		customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
		customerRepository.deleteById(id);
	}
}