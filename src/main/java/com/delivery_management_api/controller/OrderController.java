package com.delivery_management_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_management_api.dto.CreateOrderRequest;
import com.delivery_management_api.dto.OrderResponse;
import com.delivery_management_api.service.OrderService;

import jakarta.validation.Valid;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/api/orders")
	public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
		return orderService.createOrder(request);
	}
	
	@GetMapping("/api/orders")
	public List<OrderResponse> findAllOrders() {
		return orderService.findAllOrders();
	}
	
	@GetMapping("/api/orders/{id}")
	public OrderResponse findOrderById(@PathVariable Long id) {
		return orderService.findOrderById(id);
	}
	
	@DeleteMapping("/api/orders/{id}")
	public void deleteOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
	}
}
