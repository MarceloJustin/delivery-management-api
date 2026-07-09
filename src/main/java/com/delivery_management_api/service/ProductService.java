package com.delivery_management_api.service;

import java.math.BigDecimal;
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
import com.delivery_management_api.mapper.ProductMapper;
import com.delivery_management_api.repository.ProductRepository;
import com.delivery_management_api.repository.RestaurantRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private ProductMapper productMapper;

	public ProductResponse createProduct(CreateProductRequest request) {
		Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
				.orElseThrow(() -> new RestaurantNotFoundException(request.getRestaurantId()));

		Product product = new Product(request.getName(), request.getPrice(), restaurant);
		Product savedProduct = productRepository.save(product);
		return productMapper.toResponse(savedProduct);
	}

	public Page<ProductResponse> findAllProducts(Pageable pageable) {
		return productRepository
				.findAll(pageable).map(productMapper::toResponse);
	}

	public ProductResponse findProductById(Long id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
		return productMapper.toResponse(product);
	}
	
	public List<ProductResponse> findProductsByRestaurant(Long restaurantId){
		restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
		return productRepository.findByRestaurantId(restaurantId).stream()
				.map(productMapper::toResponse).toList();
	}
	
	public List<ProductResponse> findByPriceGreaterThan(BigDecimal price){
		return productRepository.findByPriceGreaterThan(price).stream()
				.map(productMapper::toResponse).toList();
	}
	
	public List<ProductResponse> findProductsAbovePrice(BigDecimal price){
		return productRepository.findProductsAbovePrice(price).stream()
				.map(productMapper::toResponse).toList();
	}
	
	public List<ProductResponse> findByPriceBetween(BigDecimal minPrice, BigDecimal maxprice){
		return productRepository.findByPriceBetween(minPrice, maxprice).stream()
				.map(productMapper::toResponse).toList();
	}
	
	public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
		Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
				.orElseThrow(() -> new RestaurantNotFoundException(request.getRestaurantId()));
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		product.setRestaurant(restaurant);
		Product updateProduct = productRepository.save(product);
		return productMapper.toResponse(updateProduct);
	}

	public void deleteProduct(Long id) {
		productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
		productRepository.deleteById(id);
	}
}