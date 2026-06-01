package com.delivery_management_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delivery_management_api.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
