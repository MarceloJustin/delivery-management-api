package com.delivery_management_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.delivery_management_api.dto.request.CreateOrderItemRequest;
import com.delivery_management_api.dto.request.CreateOrderRequest;
import com.delivery_management_api.dto.response.OrderResponse;
import com.delivery_management_api.dto.request.UpdateOrderStatusRequest;
import com.delivery_management_api.entity.Customer;
import com.delivery_management_api.entity.Order;
import com.delivery_management_api.entity.OrderItem;
import com.delivery_management_api.entity.Product;
import com.delivery_management_api.entity.Restaurant;
import com.delivery_management_api.enums.OrderStatus;
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

	@Spy
	private OrderMapper orderMapper = new OrderMapper();

	@InjectMocks
	private OrderService orderService;
	
	@Test
	void shouldCreateOrderSuccessfully() {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		Product product = createProduct(restaurant);
		
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
	
	@Test
	void shouldThrowExceptionWhenCustomerNotFound() {
		CreateOrderItemRequest requestItems = new CreateOrderItemRequest();
		requestItems.setProductId(999L);
		requestItems.setQuantity(1);
		
		CreateOrderRequest requestOrder = new CreateOrderRequest();
		requestOrder.setCustomerId(999L);
		requestOrder.setRestaurantId(999L);
		requestOrder.setItems(List.of(requestItems));
		
		when(customerRepository.findById(999L)).thenReturn(Optional.empty());
		
		assertThrows(CustomerNotFoundException.class, () -> orderService.createOrder(requestOrder));
		
		verify(orderRepository, never()).save(any(Order.class));
		
		verify(customerRepository).findById(999L);	
	}
	
	@Test
	void shouldThrowExceptionWhenRestaurantNotFound() {
		Customer customer = createCustomer();
		
		CreateOrderItemRequest requestItems = new CreateOrderItemRequest();
		requestItems.setProductId(999L);
		requestItems.setQuantity(1);
		
		CreateOrderRequest requestOrder = new CreateOrderRequest();
		requestOrder.setCustomerId(999L);
		requestOrder.setRestaurantId(999L);
		requestOrder.setItems(List.of(requestItems));
		
		when(customerRepository.findById(999L)).thenReturn(Optional.of(customer));
		when(restaurantRepository.findById(999L)).thenReturn(Optional.empty());
		
		assertThrows(RestaurantNotFoundException.class, () -> orderService.createOrder(requestOrder));
		
		verify(orderRepository, never()).save(any(Order.class));
		
		verify(restaurantRepository).findById(999L);	
	}
	
	@Test
	void shouldThrowExceptionWhenProductNotFound() {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		
		CreateOrderItemRequest requestItems = new CreateOrderItemRequest();
		requestItems.setProductId(999L);
		requestItems.setQuantity(1);
		
		CreateOrderRequest requestOrder = new CreateOrderRequest();
		requestOrder.setCustomerId(999L);
		requestOrder.setRestaurantId(999L);
		requestOrder.setItems(List.of(requestItems));
		
		when(customerRepository.findById(999L)).thenReturn(Optional.of(customer));
		when(restaurantRepository.findById(999L)).thenReturn(Optional.of(restaurant));
		when(productRepository.findById(999L)).thenReturn(Optional.empty());
		
		assertThrows(ProductNotFoundException.class, () -> orderService.createOrder(requestOrder));
		
		verify(productRepository).findById(999L);		
		verify(orderRepository).save(any(Order.class));
	}
	
	@Test
	void shouldFindOrderByIdSuccessfully() {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		Product product = createProduct(restaurant);
		
		Order savedOrder = new Order(customer, restaurant, BigDecimal.ZERO , OrderStatus.CREATED);
		ReflectionTestUtils.setField(savedOrder, "id", 1L);
		
		OrderItem orderItem = new OrderItem(savedOrder, product, 1);
		
		savedOrder.setItems(List.of(orderItem));
		
		when(orderRepository.findById(1L)).thenReturn(Optional.of(savedOrder));
		
		OrderResponse response = orderService.findOrderById(1L);
		
		assertEquals(1L, response.getId());
		
		verify(orderRepository).findById(1L);	
	}
	
	@Test
	void shouldThrowExceptionWhenOrderNotFoundById() {
		
		when(orderRepository.findById(999L)).thenReturn(Optional.empty());
		
		assertThrows(OrderNotFoundException.class, () -> orderService.findOrderById(999L));
		
		verify(orderRepository).findById(999L);		
	}
	
	@Test
	void shouldUpdateOrderStatusSuccessfully() {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		Product product = createProduct(restaurant);	
		Order order = createOrder(customer, restaurant, OrderStatus.CREATED);	
		
		UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
		request.setStatus(OrderStatus.CONFIRMED);
		
		OrderItem orderItem = new OrderItem(order, product, 1);
		
		order.setItems(List.of(orderItem));
		
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		
		OrderResponse response = orderService.updateOrderStatus(1L, request);
		
		assertNotNull(response);
		assertEquals(OrderStatus.CONFIRMED, order.getStatus());
		
		verify(orderRepository).findById(1L);
		verify(orderRepository).save(order);	
	}
	
	@Test
	void shouldThrowExceptionWhenOrderStatusTransitionIsInvalid() {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		Product product = createProduct(restaurant);	
		Order order = createOrder(customer, restaurant, OrderStatus.CREATED);	
		
		UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
		request.setStatus(OrderStatus.DELIVERED);
		
		OrderItem orderItem = new OrderItem(order, product, 1);
		
		order.setItems(List.of(orderItem));
		
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		
		assertThrows(InvalidOrderStatusException.class, () -> orderService.updateOrderStatus(1L, request));
		
		verify(orderRepository).findById(1L);
		verify(orderRepository, never()).save(any(Order.class));
	}

	@Test
	void shouldUpdateOrderStatusFromConfirmedToPreparing() {
	    Customer customer = createCustomer();
	    Restaurant restaurant = createRestaurant();
	    Product product = createProduct(restaurant);
	    Order order = createOrder(customer, restaurant, OrderStatus.CONFIRMED);

	    UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
	    request.setStatus(OrderStatus.PREPARING);

	    OrderItem orderItem = new OrderItem(order, product, 1);
	    order.setItems(List.of(orderItem));

	    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

	    OrderResponse response = orderService.updateOrderStatus(1L, request);

	    assertNotNull(response);
	    assertEquals(OrderStatus.PREPARING, order.getStatus());

	    verify(orderRepository).findById(1L);
	    verify(orderRepository).save(order);
	}
	
	@Test
	void shouldThrowExceptionWhenConfirmedTransitionIsInvalid() {
	    Customer customer = createCustomer();
	    Restaurant restaurant = createRestaurant();
	    Product product = createProduct(restaurant);

	    Order order = createOrder(customer, restaurant, OrderStatus.CONFIRMED);

	    UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
	    request.setStatus(OrderStatus.DELIVERED);

	    OrderItem orderItem = new OrderItem(order, product, 1);
	    order.setItems(List.of(orderItem));

	    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

	    assertThrows(InvalidOrderStatusException.class,() -> orderService.updateOrderStatus(1L, request));

	    verify(orderRepository, never()).save(any(Order.class));
	}
	
	@Test
	void shouldUpdateOrderStatusFromPreparingToOutForDelivery() {
	    Customer customer = createCustomer();
	    Restaurant restaurant = createRestaurant();
	    Product product = createProduct(restaurant);
	    Order order = createOrder(customer, restaurant, OrderStatus.PREPARING);

	    UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
	    request.setStatus(OrderStatus.OUT_FOR_DELIVERY);

	    OrderItem orderItem = new OrderItem(order, product, 1);
	    order.setItems(List.of(orderItem));

	    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

	    OrderResponse response = orderService.updateOrderStatus(1L, request);

	    assertNotNull(response);
	    assertEquals(OrderStatus.OUT_FOR_DELIVERY, order.getStatus());

	    verify(orderRepository).findById(1L);
	    verify(orderRepository).save(order);
	}
	
	@Test
	void shouldThrowExceptionWhenPreparingTransitionIsInvalid() {
	    Customer customer = createCustomer();
	    Restaurant restaurant = createRestaurant();
	    Product product = createProduct(restaurant);

	    Order order = createOrder(customer, restaurant, OrderStatus.PREPARING);

	    UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();request.setStatus(OrderStatus.DELIVERED);

	    OrderItem orderItem = new OrderItem(order, product, 1);
	    order.setItems(List.of(orderItem));

	    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

	    assertThrows(InvalidOrderStatusException.class,() -> orderService.updateOrderStatus(1L, request));

	    verify(orderRepository, never()).save(any(Order.class));
	}
	
	@Test
	void shouldUpdateOrderStatusFromOutForDeliveryToDelivered() {
	    Customer customer = createCustomer();
	    Restaurant restaurant = createRestaurant();
	    Product product = createProduct(restaurant);
	    Order order = createOrder(customer, restaurant, OrderStatus.OUT_FOR_DELIVERY);

	    UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
	    request.setStatus(OrderStatus.DELIVERED);

	    OrderItem orderItem = new OrderItem(order, product, 1);
	    order.setItems(List.of(orderItem));

	    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

	    OrderResponse response = orderService.updateOrderStatus(1L, request);

	    assertNotNull(response);
	    assertEquals(OrderStatus.DELIVERED, order.getStatus());

	    verify(orderRepository).findById(1L);
	    verify(orderRepository).save(order);
	}
	
	@Test
	void shouldThrowExceptionWhenOutForDeliveryTransitionIsInvalid() {
	    Customer customer = createCustomer();
	    Restaurant restaurant = createRestaurant();
	    Product product = createProduct(restaurant);

	    Order order =createOrder(customer, restaurant, OrderStatus.OUT_FOR_DELIVERY);

	    UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
	    request.setStatus(OrderStatus.CONFIRMED);

	    OrderItem orderItem = new OrderItem(order, product, 1);
	    order.setItems(List.of(orderItem));

	    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

	    assertThrows(InvalidOrderStatusException.class,() -> orderService.updateOrderStatus(1L, request));

	    verify(orderRepository, never()).save(any(Order.class));
	}
	
	@Test
	void shouldCancelConfirmedOrderSuccessfully() {
	    Customer customer = createCustomer();
	    Restaurant restaurant = createRestaurant();
	    Order order = createOrder(customer, restaurant, OrderStatus.CONFIRMED);

	    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

	    orderService.cancelOrder(1L);

	    assertEquals(OrderStatus.CANCELLED, order.getStatus());

	    verify(orderRepository).findById(1L);
	    verify(orderRepository).save(order);
	}
	
	@Test
	void shouldThrowExceptionWhenUpdatingCancelledOrder() {
	    Customer customer = createCustomer();
	    Restaurant restaurant = createRestaurant();
	    Product product = createProduct(restaurant);

	    Order order = createOrder(customer, restaurant, OrderStatus.CANCELLED);

	    UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
	    request.setStatus(OrderStatus.CONFIRMED);

	    OrderItem orderItem = new OrderItem(order, product, 1);
	    order.setItems(List.of(orderItem));

	    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

	    assertThrows(InvalidOrderStatusException.class,() -> orderService.updateOrderStatus(1L, request));

	    verify(orderRepository, never()).save(any(Order.class));
	}

	@Test
	void shouldCancelOrderSuccessfully() {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		Order order = createOrder(customer, restaurant, OrderStatus.CREATED);
		
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		
		orderService.cancelOrder(1L);
		
		assertEquals(OrderStatus.CANCELLED, order.getStatus());
		
		verify(orderRepository).findById(1L);
		verify(orderRepository).save(order);
	}
	
	@Test
	void shouldThrowExceptionWhenOrderNotFoundForCancellation() {
		
		when(orderRepository.findById(999L)).thenReturn(Optional.empty());
		
		assertThrows(OrderNotFoundException.class, () -> orderService.cancelOrder(999L));
		
		verify(orderRepository).findById(999L);
		verify(orderRepository, never()).save(any(Order.class));
	}
	
	@Test
	void shouldThrowExceptionWhenCancellationIsNotAllowed() {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		Order order = createOrder(customer, restaurant, OrderStatus.PREPARING);		
		
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		
		assertThrows(OrderCancellationNotAllowedException.class, () -> orderService.cancelOrder(1L));
		
		verify(orderRepository).findById(1L);
		verify(orderRepository, never()).save(any(Order.class));
	}	
	
	private Customer createCustomer() {
		Customer customer = new Customer("Marcelo Justin", "marcelo@email.com");
		ReflectionTestUtils.setField(customer, "id", 1L);
		
		return customer;
	}
	
	private Restaurant createRestaurant() {
		Restaurant restaurant = new Restaurant("Burger King", "Hamburger", BigDecimal.valueOf(8.00));
		ReflectionTestUtils.setField(restaurant, "id", 1L);
		
		return restaurant;
	}
	
	private Product createProduct(Restaurant restaurant) {
		Product product = new Product("Hamburger Carne", BigDecimal.valueOf(15.00), restaurant);
		ReflectionTestUtils.setField(product, "id", 1L);
		
		return product;
	}
	
	private Order createOrder(Customer customer, Restaurant restaurant, OrderStatus status) {
		Order order = new Order(customer, restaurant, BigDecimal.valueOf(15.00) , status);
		ReflectionTestUtils.setField(order, "id", 1L);
		
		return order;
	}
}