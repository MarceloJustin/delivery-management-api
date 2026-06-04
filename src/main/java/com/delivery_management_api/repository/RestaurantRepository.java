package com.delivery_management_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delivery_management_api.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{
	
	List<Restaurant> findByNameContainingIgnoreCase(String name);
	
}
