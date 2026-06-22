package com.delivery_management_api.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class OrderControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

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

	@Test
	void shouldCreateOrder() throws Exception {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		Product product = createProduct(restaurant);

		String requestBody = """
				{
				  "customerId": %d,
				  "restaurantId": %d,
				  "items": [
				    {
				      "productId": %d,
				      "quantity": 1
				    }
				  ]
				}""".formatted(customer.getId(), restaurant.getId(), product.getId());

		mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.customerId").value(customer.getId()))
				.andExpect(jsonPath("$.restaurantId").value(restaurant.getId()))
				.andExpect(jsonPath("$.status").value("CREATED")).andExpect(jsonPath("$.totalAmount").value(43.80))
				.andExpect(jsonPath("$.items[0].productId").value(product.getId()))
				.andExpect(jsonPath("$.items[0].quantity").value(1));
		
		Order created = orderRepository.findAll().getFirst();

		assertEquals(customer.getId(),created.getCustomer().getId());
	}

	@Test
	void shouldReturnBadRequestWhenItemsIsEmpty() throws Exception {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();

		String requestBody = """
				{
				  "customerId": %d,
				  "restaurantId": %d,
				  "items": []
				}""".formatted(customer.getId(), restaurant.getId());

		mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnBadRequestWhenCustomerIdIsNull() throws Exception {
		Restaurant restaurant = createRestaurant();
		Product product = createProduct(restaurant);

		String requestBody = """
				{
				  "customerId": null,
				  "restaurantId": %d,
				  "items": [
				  	{
				      "productId": %d,
				 	  "quantity": 1
				 	}
				  ]
				}""".formatted(restaurant.getId(), product.getId());

		mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnOrderList() throws Exception {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		createOrderWithItems(customer, restaurant, OrderStatus.CREATED);

		mockMvc.perform(get("/api/orders")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(1));
	}

	@Test
	void shouldReturnNotFoundWhenOrderDoesNotExists() throws Exception {
		mockMvc.perform(get("/api/orders/99999")).andExpect(status().isNotFound());
	}

	@Test
	void shouldReturnOrderWhenExists() throws Exception {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		Order order = createOrderWithItems(customer, restaurant, OrderStatus.CREATED);
		OrderItem item = order.getItems().getFirst();

		mockMvc.perform(get("/api/orders/" + order.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(order.getId()))
				.andExpect(jsonPath("$.customerId").value(customer.getId()))
				.andExpect(jsonPath("$.restaurantId").value(restaurant.getId()))
				.andExpect(jsonPath("$.deliveryFee").value(5.0)).andExpect(jsonPath("$.totalAmount").value(43.80))
				.andExpect(jsonPath("$.status").value("CREATED"))
				.andExpect(jsonPath("$.items[0].productId").value(item.getProduct().getId()))
				.andExpect(jsonPath("$.items[0].productName").value(item.getProduct().getName()))
				.andExpect(jsonPath("$.items[0].quantity").value(item.getQuantity()));
	}

	@Test
	void shouldReturnOrderByStatus() throws Exception {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		createOrderWithItems(customer, restaurant, OrderStatus.CREATED);
		createOrderWithItems(customer, restaurant, OrderStatus.CREATED);
		createOrderWithItems(customer, restaurant, OrderStatus.DELIVERED);

		mockMvc.perform(get("/api/orders/status/" + "CREATED")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].status").value("CREATED"))
				.andExpect(jsonPath("$.content[1].status").value("CREATED"))
				.andExpect(jsonPath("$.totalElements").value(2)).andExpect(jsonPath("$.content.length()").value(2));
	}

	@Test
	void shouldUpdateOrderStatus() throws Exception {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		Order order = createOrderWithItems(customer, restaurant, OrderStatus.CREATED);

		String requestBody = """
				{
					"status": "CONFIRMED"
				}
				""";

		mockMvc.perform(patch("/api/orders/" + order.getId() + "/status").contentType(MediaType.APPLICATION_JSON)
				.content(requestBody)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(order.getId())).andExpect(jsonPath("$.status").value("CONFIRMED"));

		Order updateOrder = orderRepository.findById(order.getId()).orElseThrow();

		assertEquals(OrderStatus.CONFIRMED, updateOrder.getStatus());
	}

	@Test
	void shouldReturnNotFoundWhenUpdatingNonExistingOrder() throws Exception {
		String requestBody = """
				{
					"status": "CONFIRMED"
				}
				""";

		mockMvc.perform(
				patch("/api/orders/" + 999 + "/status").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldReturnConflictWhenStatusTransitionIsInvalid() throws Exception {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		Order order = createOrderWithItems(customer, restaurant, OrderStatus.CREATED);

		String requestBody = """
				{
					"status": "PREPARING"
				}
				""";

		mockMvc.perform(patch("/api/orders/" + order.getId() + "/status").contentType(MediaType.APPLICATION_JSON)
				.content(requestBody)).andExpect(status().isConflict());
	}

	@Test
	void shouldCancelOrder() throws Exception {
		Customer customer = createCustomer();
		Restaurant restaurant = createRestaurant();
		Order order = createOrderWithItems(customer, restaurant, OrderStatus.CREATED);

		mockMvc.perform(patch("/api/orders/" + order.getId() + "/cancel"))
				.andExpect(status().isNoContent());

		Order cancelledOrder = orderRepository.findById(order.getId()).orElseThrow();

		assertEquals(OrderStatus.CANCELLED, cancelledOrder.getStatus());
	}

	@Test
	void shouldReturnNotFoundtWhenCancelingNonExistingOrder() throws Exception {
		mockMvc.perform(patch("/api/orders/" + 999 + "/cancel")).andExpect(status().isNotFound());
	}

	private Order createOrderWithItems(Customer customer, Restaurant restaurant, OrderStatus status) {
		Product product = createProduct(restaurant);
		Order order = orderRepository.save(new Order(customer, restaurant, BigDecimal.valueOf(43.80), status));
		OrderItem item = orderItemRepository.save(new OrderItem(order, product, 1));
		order.setItems(new ArrayList<>(List.of(item)));
		;
		return order;
	}

	private Customer createCustomer() {
		Customer customer = customerRepository.save(new Customer("Marcelo", "marcelo@email.com"));
		return customer;
	}

	private Restaurant createRestaurant() {
		Restaurant restaurant = restaurantRepository
				.save(new Restaurant("Pizzaria do Marcelo", "Pizza", BigDecimal.valueOf(5.0)));
		return restaurant;
	}

	private Product createProduct(Restaurant restaurant) {
		Product product = productRepository.save(new Product("Pizza 4Queijos", BigDecimal.valueOf(38.80), restaurant));
		return product;
	}
}