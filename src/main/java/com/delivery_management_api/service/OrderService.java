package com.delivery_management_api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.delivery_management_api.dto.request.CreateOrderItemRequest;
import com.delivery_management_api.dto.request.CreateOrderRequest;
import com.delivery_management_api.dto.response.OrderItemResponse;
import com.delivery_management_api.dto.response.OrderResponse;
import com.delivery_management_api.dto.request.UpdateOrderStatusRequest;
import com.delivery_management_api.entity.Customer;
import com.delivery_management_api.entity.Order;
import com.delivery_management_api.entity.OrderItem;
import com.delivery_management_api.entity.Product;
import com.delivery_management_api.entity.Restaurant;
import com.delivery_management_api.entity.User;
import com.delivery_management_api.enums.OrderStatus;
import com.delivery_management_api.enums.Role;
import com.delivery_management_api.exception.CustomerNotFoundException;
import com.delivery_management_api.exception.InvalidOrderStatusException;
import com.delivery_management_api.exception.OrderCancellationNotAllowedException;
import com.delivery_management_api.exception.OrderNotFoundException;
import com.delivery_management_api.exception.ProductNotFoundException;
import com.delivery_management_api.exception.RestaurantNotFoundException;
import com.delivery_management_api.mapper.OrderMapper;
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

	@Autowired
	private OrderMapper orderMapper;

	@Transactional
	public OrderResponse createOrder(CreateOrderRequest request) {
		User currentUser = getCurrentUser();

		Customer customer = currentUser.getRole() == Role.ADMIN
				? customerRepository.findById(request.getCustomerId())
						.orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()))
				: getCurrentCustomer();

		Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
				.orElseThrow(() -> new RestaurantNotFoundException(request.getRestaurantId()));
		
		Order order = new Order(customer, restaurant, BigDecimal.ZERO, OrderStatus.CREATED);
		
		Order savedOrder = orderRepository.save(order);
		
		BigDecimal totalAmount = BigDecimal.ZERO;

		List<OrderItem> orderItems = new ArrayList<>();
		List<OrderItemResponse> responseItems = new ArrayList<>();

		for(CreateOrderItemRequest itemRequest : request.getItems()) {
			Product product = productRepository.findById(itemRequest.getProductId())
					.orElseThrow(() -> new ProductNotFoundException(itemRequest.getProductId()));

			OrderItem orderItem = new OrderItem(savedOrder, product, itemRequest.getQuantity());
			orderItemRepository.save(orderItem);
			BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
			totalAmount = totalAmount.add(subtotal);
			orderItems.add(orderItem);
			responseItems.add(orderMapper.toItemResponse(orderItem));
		}

		totalAmount = totalAmount.add(restaurant.getDeliveryFee());

		savedOrder.setTotalAmount(totalAmount);
		savedOrder.setItems(orderItems);

		savedOrder = orderRepository.save(savedOrder);
		
		return new OrderResponse(savedOrder.getId(), customer.getId(), customer.getName(), restaurant.getId(), 
				restaurant.getName(),restaurant.getDeliveryFee(), savedOrder.getTotalAmount(), savedOrder.getStatus(), responseItems);
	}
	
	public Page<OrderResponse> findAllOrders(Pageable pageable) {
		if (getCurrentUser().getRole() == Role.ADMIN) {
			return orderRepository.findAll(pageable).map(orderMapper::toResponse);
		}

		Customer customer = getCurrentCustomer();
		return orderRepository.findByCustomerId(customer.getId(), pageable).map(orderMapper::toResponse);
	}

	public OrderResponse findOrderById(Long id) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
		checkOwnership(order);
		return orderMapper.toResponse(order);
	}

	public Page<OrderResponse> findByStatus(OrderStatus status, Pageable pageable) {
		if (getCurrentUser().getRole() == Role.ADMIN) {
			return orderRepository.findByStatus(status, pageable).map(orderMapper::toResponse);
		}

		Customer customer = getCurrentCustomer();
		return orderRepository.findByCustomerIdAndStatus(customer.getId(), status, pageable).map(orderMapper::toResponse);
	}
	
	public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
		OrderStatus currentStatus = order.getStatus();
		OrderStatus newStatus = request.getStatus();
		
		if(!isValidTransition(currentStatus, newStatus)) {
			throw new InvalidOrderStatusException(currentStatus.name(), newStatus.name());
		}
		order.setStatus(newStatus);
		
		orderRepository.save(order);
		return orderMapper.toResponse(order);
	}

	public void cancelOrder(Long id) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
		checkOwnership(order);

		if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.CONFIRMED) {
			throw new OrderCancellationNotAllowedException();
		}
		
		order.setStatus(OrderStatus.CANCELLED);
		orderRepository.save(order);
	}
	
	private User getCurrentUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	private Customer getCurrentCustomer() {
		return customerRepository.findByUser(getCurrentUser())
				.orElseThrow(() -> new AccessDeniedException("You do not have permission to access this resource"));
	}

	private void checkOwnership(Order order) {
		User currentUser = getCurrentUser();

		if (currentUser.getRole() == Role.ADMIN) {
			return;
		}

		Customer customer = getCurrentCustomer();

		if (!order.getCustomer().getId().equals(customer.getId())) {
			throw new AccessDeniedException("You do not have permission to access this resource");
		}
	}

	private boolean isValidTransition(OrderStatus current, OrderStatus next) {
		switch (current) {
		case CREATED:
			return next == OrderStatus.CONFIRMED;
		
		case CONFIRMED:
			return next == OrderStatus.PREPARING;
			
		case PREPARING:
			return next == OrderStatus.OUT_FOR_DELIVERY;
		
		case OUT_FOR_DELIVERY:
			return next == OrderStatus.DELIVERED;
		
		default:
			return false;
		}
	}
}