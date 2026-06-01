package com.delivery_management_api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delivery_management_api.dto.CreateOrderItemRequest;
import com.delivery_management_api.dto.CreateOrderRequest;
import com.delivery_management_api.dto.OrderItemResponse;
import com.delivery_management_api.dto.OrderResponse;
import com.delivery_management_api.entity.Customer;
import com.delivery_management_api.entity.Order;
import com.delivery_management_api.entity.OrderItem;
import com.delivery_management_api.entity.Product;
import com.delivery_management_api.entity.Restaurant;
import com.delivery_management_api.enums.OrderStatus;
import com.delivery_management_api.exception.CustomerNotFoundException;
import com.delivery_management_api.exception.ProductNotFoundException;
import com.delivery_management_api.exception.RestaurantNotFoundException;
import com.delivery_management_api.repository.CustomerRepository;
import com.delivery_management_api.repository.OrderItemRepository;
import com.delivery_management_api.repository.OrderRepository;
import com.delivery_management_api.repository.ProductRepository;
import com.delivery_management_api.repository.RestaurantRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Transactional
	public OrderResponse createOrder(CreateOrderRequest request) {
		Customer customer = customerRepository.findById(request.getCustomerId())
				.orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));
		
		Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
				.orElseThrow(() -> new RestaurantNotFoundException(request.getRestaurantId()));
		
		Order order = new Order(customer, restaurant, BigDecimal.ZERO, OrderStatus.CREATED);
		
		Order savedOrder = orderRepository.save(order);
		
		BigDecimal totalAmount = BigDecimal.ZERO;
		
		List<OrderItemResponse> responseItems = new ArrayList<>();
		
		for(CreateOrderItemRequest itemRequest : request.getItems()) {
			Product product = productRepository.findById(itemRequest.getProductId())
					.orElseThrow(() -> new ProductNotFoundException(itemRequest.getProductId()));
			
			OrderItem orderItem = new OrderItem(savedOrder, product, itemRequest.getQuantity());
			orderItemRepository.save(orderItem);
			BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
			totalAmount = totalAmount.add(subtotal);
			responseItems.add(new OrderItemResponse(product.getId(), product.getName(), itemRequest.getQuantity()));
		}
		savedOrder.setTotalAmount(totalAmount);
		
		savedOrder = orderRepository.save(savedOrder);
		
		return new OrderResponse(savedOrder.getId(), customer.getId(), customer.getName(), restaurant.getId(), 
				restaurant.getName(), savedOrder.getTotalAmount(), savedOrder.getStatus(), responseItems);
	}
}
