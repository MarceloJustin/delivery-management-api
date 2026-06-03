package com.delivery_management_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_management_api.dto.CreateOrderRequest;
import com.delivery_management_api.dto.ErrorResponse;
import com.delivery_management_api.dto.OrderResponse;
import com.delivery_management_api.dto.UpdateOrderStatusRequest;
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
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Order created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))) })
	public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
	}

	@GetMapping("/api/orders")
	@Operation(summary = "List orders", description = "Returns all registered orders")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Orders found") })
	public ResponseEntity<List<OrderResponse>> findAllOrders() {
		return ResponseEntity.ok(orderService.findAllOrders());
	}

	@GetMapping("/api/orders/{id}")
	@Operation(summary = "Find order by ID", description = "Returns an order by its ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Orders found"),
			@ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<OrderResponse> findOrderById(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.findOrderById(id));
	}
	
	@PatchMapping("/api/orders/{id}/status")
	@Operation(summary = "Update order status", description = "Updates the status of an existing order")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
			@ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "409", description = "Invalid order status transition", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequest request) {
		return ResponseEntity.ok(orderService.updateOrderStatus(id, request));
	}

	@PatchMapping("/api/orders/{id}/cancel")
	@Operation(summary = "Cancel order", description = "Cancels an order by ID")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Order canceled successfully"),
			@ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
		orderService.cancelOrder(id);
		return ResponseEntity.noContent().build();
	}
}