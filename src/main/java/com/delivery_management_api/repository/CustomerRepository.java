package com.delivery_management_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delivery_management_api.entity.Customer;
import com.delivery_management_api.entity.User;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByUser(User user);

}
