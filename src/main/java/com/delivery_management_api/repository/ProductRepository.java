package com.delivery_management_api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delivery_management_api.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	//Spring Data
	List<Product> findByRestaurantId(Long restaurantId);
	
	List<Product> findByPriceGreaterThan(BigDecimal price);
	
	List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
	
	//JPQL
	@Query("SELECT p FROM Product p WHERE p.price > :price")
	List<Product> findProductsAbovePrice(@Param("price") BigDecimal price);
}