package com.delivery_management_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.delivery_management_api.dto.CreateProductRequest;
import com.delivery_management_api.dto.ProductResponse;
import com.delivery_management_api.dto.UpdateProductRequest;
import com.delivery_management_api.entity.Product;
import com.delivery_management_api.entity.Restaurant;
import com.delivery_management_api.exception.ProductNotFoundException;
import com.delivery_management_api.exception.RestaurantNotFoundException;
import com.delivery_management_api.repository.ProductRepository;
import com.delivery_management_api.repository.RestaurantRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	public ProductResponse createProduct(CreateProductRequest request) {
		Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
				.orElseThrow(() -> new RestaurantNotFoundException(request.getRestaurantId()));

		Product product = new Product(request.getName(), request.getPrice(), restaurant);
		Product savedProduct = productRepository.save(product);
		return new ProductResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice(),
				savedProduct.getRestaurant().getId(), savedProduct.getRestaurant().getName());
	}

	public Page<ProductResponse> findAllProducts(Pageable pageable) {
		return productRepository
				.findAll(pageable).map(product -> new ProductResponse(product.getId(), product.getName(),
						product.getPrice(), product.getRestaurant().getId(), product.getRestaurant().getName()));
	}

	public ProductResponse findProductById(Long id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
		return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
				product.getRestaurant().getId(), product.getRestaurant().getName());
	}

	public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(() -> new RestaurantNotFoundException(request.getRestaurantId()));
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		product.setRestaurant(restaurant);
		Product updateProduct = productRepository.save(product);
		return new ProductResponse(updateProduct.getId(), updateProduct.getName(), updateProduct.getPrice(),
				updateProduct.getRestaurant().getId(), updateProduct.getRestaurant().getName());
	}

	public void deleteProduct(Long id) {
		productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
		productRepository.deleteById(id);
	}
}