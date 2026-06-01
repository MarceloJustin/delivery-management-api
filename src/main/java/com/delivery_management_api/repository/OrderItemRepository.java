package com.delivery_management_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delivery_management_api.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
}
