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
import com.delivery_management_api.dto.ErrorResponse;
import com.delivery_management_api.dto.OrderResponse;
import com.delivery_management_api.dto.ValidationErrorResponse;
import com.delivery_management_api.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Orders", description = "Operations related to order management")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/api/orders")
	@Operation(summary = "Create order", description = "Creates a new order")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Order created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))) })
	public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
		return orderService.createOrder(request);
	}

	@GetMapping("/api/orders")
	@Operation(summary = "List orders", description = "Returns all registered orders")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Orders found") })
	public List<OrderResponse> findAllOrders() {
		return orderService.findAllOrders();
	}

	@GetMapping("/api/orders/{id}")
	@Operation(summary = "Find order by ID", description = "Returns an order by its ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Orders found"),
			@ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public OrderResponse findOrderById(@PathVariable Long id) {
		return orderService.findOrderById(id);
	}

	@DeleteMapping("/api/orders/{id}")
	@Operation(summary = "Delete order", description = "Deletes an order by ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Order deleted"),
			@ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public void deleteOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
	}
}
