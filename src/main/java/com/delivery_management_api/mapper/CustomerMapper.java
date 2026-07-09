package com.delivery_management_api.mapper;

import org.springframework.stereotype.Component;

import com.delivery_management_api.dto.response.CustomerResponse;
import com.delivery_management_api.entity.Customer;

@Component
public class CustomerMapper {

	public CustomerResponse toResponse(Customer customer) {
		return new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail());
	}

}
