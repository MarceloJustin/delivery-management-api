package com.delivery_management_api.service;

import com.delivery_management_api.dto.request.CreateCustomerRequest;
import com.delivery_management_api.dto.response.CustomerResponse;
import com.delivery_management_api.dto.request.UpdateCustomerRequest;
import com.delivery_management_api.entity.Customer;
import com.delivery_management_api.exception.CustomerNotFoundException;
import com.delivery_management_api.mapper.CustomerMapper;
import com.delivery_management_api.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer(request.getName(), request.getEmail());
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    public Page<CustomerResponse> findAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(customerMapper::toResponse);
    }

    public CustomerResponse findCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        return customerMapper.toResponse(customer);
    }

    public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        customerRepository.deleteById(id);
    }
}