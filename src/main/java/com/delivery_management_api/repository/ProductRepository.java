package com.delivery_management_api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delivery_management_api.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	List<Product> findByRestaurantId(Long restaurantId);
	
	List<Product> findByPriceGreaterThan(BigDecimal price);
	
	List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
	
}