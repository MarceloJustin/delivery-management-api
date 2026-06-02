package com.delivery_management_api.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private BigDecimal price;
	
	@ManyToOne
	@JoinColumn(name="restaurant_id")
	private Restaurant restaurant;
	
	public Product() {
	}

	public Product(String name, BigDecimal price, Restaurant restaurant) {
		this.name = name;
		this.price = price;
		this.restaurant = restaurant;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getId() {
		return id;
	}
	
	public Restaurant getRestaurant() {
	    return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
	    this.restaurant = restaurant;
	}
}