package com.delivery_management_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.delivery_management_api.DeliveryManagementApiApplication;
import com.delivery_management_api.dto.CreateCustomerRequest;
import com.delivery_management_api.dto.CustomerResponse;
import com.delivery_management_api.entity.Customer;
import com.delivery_management_api.repository.CustomerRepository;

@Service
public class CustomerService {

	private final DeliveryManagementApiApplication deliveryManagementApiApplication;

	@Autowired
	private CustomerRepository customerRepository;

	CustomerService(DeliveryManagementApiApplication deliveryManagementApiApplication) {
		this.deliveryManagementApiApplication = deliveryManagementApiApplication;
	}

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
}
