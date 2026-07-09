package com.delivery_management_api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.delivery_management_api.dto.OrderItemResponse;
import com.delivery_management_api.dto.OrderResponse;
import com.delivery_management_api.entity.Order;
import com.delivery_management_api.entity.OrderItem;

@Component
public class OrderMapper {

	public OrderItemResponse toItemResponse(OrderItem item) {
		return new OrderItemResponse(item.getProduct().getId(), item.getProduct().getName(), item.getQuantity());
	}

	public OrderResponse toResponse(Order order) {
		List<OrderItemResponse> items = order.getItems().stream().map(this::toItemResponse).toList();

		return new OrderResponse(order.getId(), order.getCustomer().getId(), order.getCustomer().getName(),
				order.getRestaurant().getId(), order.getRestaurant().getName(), order.getRestaurant().getDeliveryFee(),
				order.getTotalAmount(), order.getStatus(), items);
	}

}