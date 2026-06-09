package com.delivery_management_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.delivery_management_api.dto.CreateOrderItemRequest;
import com.delivery_management_api.dto.CreateOrderRequest;
import com.delivery_management_api.dto.OrderResponse;
import com.delivery_management_api.entity.Customer;
import com.delivery_management_api.entity.Order;
import com.delivery_management_api.entity.OrderItem;
import com.delivery_management_api.entity.Product;
import com.delivery_management_api.entity.Restaurant;
import com.delivery_management_api.enums.OrderStatus;
import com.delivery_management_api.repository.CustomerRepository;
import com.delivery_management_api.repository.OrderItemRepository;
import com.delivery_management_api.repository.OrderRepository;
import com.delivery_management_api.repository.ProductRepository;
import com.delivery_management_api.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
	
	@Mock
	private RestaurantRepository restaurantRepository;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private CustomerRepository customerRepository;
	
	@Mock
	private OrderItemRepository orderItemRepository;
	
	@InjectMocks
	private OrderService orderService;
	
	@Test
	void shouldCreateOrderSuccessfully() {
		
		Customer customer = new Customer("Marcelo Justin", "marcelo@email.com");
		ReflectionTestUtils.setField(customer, "id", 1L);
		
		Restaurant restaurant = new Restaurant("Burger King", "Hamburger", BigDecimal.valueOf(8.00));
		ReflectionTestUtils.setField(restaurant, "id", 1L);
		
		Product product = new Product("Hamburger Carne", BigDecimal.valueOf(15.00), restaurant);
		ReflectionTestUtils.setField(product, "id", 1L);
		
		CreateOrderItemRequest requestItems = new CreateOrderItemRequest();
		requestItems.setProductId(1L);
		requestItems.setQuantity(1);
		
		CreateOrderRequest requestOrder = new CreateOrderRequest();
		requestOrder.setCustomerId(1L);
		requestOrder.setRestaurantId(1L);
		requestOrder.setItems(List.of(requestItems));
		
		Order savedOrder = new Order(customer, restaurant, BigDecimal.ZERO , OrderStatus.CREATED);
		ReflectionTestUtils.setField(savedOrder, "id", 1L);
		
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
		
		OrderResponse response = orderService.createOrder(requestOrder);
		
		assertNotNull(response);
		assertEquals(1L, response.getId());
		assertEquals("Marcelo Justin", response.getCustomerName());
		assertEquals(1L, response.getCustomerId());
		assertEquals("Burger King", response.getRestaurantName());
		assertEquals(1L, response.getRestaurantId());
		assertEquals(BigDecimal.valueOf(8.00), response.getDeliveryFee());
		assertEquals(BigDecimal.valueOf(23.00), response.getTotalAmount());
		assertEquals(OrderStatus.CREATED, response.getStatus());
		assertEquals(1, response.getItems().size());
		assertEquals("Hamburger Carne", response.getItems().get(0).getProductName());
		assertEquals(1, response.getItems().get(0).getQuantity());
		
		verify(customerRepository).findById(1L);
		verify(productRepository).findById(1L);
		verify(restaurantRepository).findById(1L);
		verify(orderItemRepository).save(any(OrderItem.class));
		verify(orderRepository, times(2)).save(any(Order.class));
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
