package com.delivery_management_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_management_api.dto.CreateProductRequest;
import com.delivery_management_api.dto.ProductResponse;
import com.delivery_management_api.dto.UpdateProductRequest;
import com.delivery_management_api.service.ProductService;

import jakarta.validation.Valid;

@RestController
public class ProductController {

	@Autowired
    private ProductService productService;

	@PostMapping("/api/products")
	public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
		return productService.createProduct(request);
	}
	
	@GetMapping("/api/products")
	public List<ProductResponse> findAllProducts() {
		return productService.findAllProduct();
	}
	
	@GetMapping("/api/products/{id}")
	public ProductResponse findProductById(@PathVariable Long id) {
		return productService.findProductById(id);
	}
	
	@PutMapping("/api/products/{id}")
	public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
		return productService.updateProduct(id, request);
	}

	@DeleteMapping("/api/products/{id}")
	public void deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
	}
}