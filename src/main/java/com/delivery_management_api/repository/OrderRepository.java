package com.delivery_management_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.delivery_management_api.entity.Order;
import com.delivery_management_api.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long>{

	Page<Order> findByStatus(OrderStatus status, Pageable pageable);

	Page<Order> findByCustomerId(Long customerId, Pageable pageable);

}
