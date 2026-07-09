package com.delivery_management_api.mapper;

import org.springframework.stereotype.Component;

import com.delivery_management_api.dto.response.ProductResponse;
import com.delivery_management_api.entity.Product;

@Component
public class ProductMapper {

	public ProductResponse toResponse(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
				product.getRestaurant().getId(), product.getRestaurant().getName());
	}

}