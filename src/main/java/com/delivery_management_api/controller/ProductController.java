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
import com.delivery_management_api.dto.ErrorResponse;
import com.delivery_management_api.dto.ProductResponse;
import com.delivery_management_api.dto.UpdateProductRequest;
import com.delivery_management_api.dto.ValidationErrorResponse;
import com.delivery_management_api.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Products", description = "Operations related to product management")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("/api/products")
	@Operation(summary = "Create product", description = "Creates a new product")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Product created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))) })
	public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
		return productService.createProduct(request);
	}

	@GetMapping("/api/products")
	@Operation(summary = "List products", description = "Returns all registered products")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Products found") })
	public List<ProductResponse> findAllProducts() {
		return productService.findAllProduct();
	}

	@GetMapping("/api/products/{id}")
	@Operation(summary = "Find product by ID", description = "Returns a product by its ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Product found"),
			@ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public ProductResponse findProductById(@PathVariable Long id) {
		return productService.findProductById(id);
	}

	@PutMapping("/api/products/{id}")
	@Operation(summary = "Update product", description = "Updates an existing product")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Product updated"),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
		return productService.updateProduct(id, request);
	}

	@DeleteMapping("/api/products/{id}")
	@Operation(summary = "Delete product", description = "Deletes a product by ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Product deleted"),
			@ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public void deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
	}
}