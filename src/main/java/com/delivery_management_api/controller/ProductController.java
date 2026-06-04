package com.delivery_management_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_management_api.dto.CreateProductRequest;
import com.delivery_management_api.dto.ErrorResponse;
import com.delivery_management_api.dto.ProductResponse;
import com.delivery_management_api.dto.UpdateProductRequest;
import com.delivery_management_api.dto.ValidationErrorResponse;
import com.delivery_management_api.repository.ProductRepository;
import com.delivery_management_api.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Operations related to product management")
public class ProductController {

    private final ProductRepository productRepository;

	@Autowired
	private ProductService productService;

    ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

	@PostMapping
	@Operation(summary = "Create product", description = "Creates a new product")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Product created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))) })
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
	}

	@GetMapping
	@Operation(summary = "List products", description = "Returns all registered products")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Products found") })
	public ResponseEntity<Page<ProductResponse>> findAllProducts(Pageable pageable) {
		return ResponseEntity.ok(productService.findAllProducts(pageable));
	}
	
	@GetMapping("/restaurant/{restaurantId}")
	@Operation(summary = "List of products by restaurant", description = "Returns all products from a specific restaurant")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Products found"),
			@ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<List<ProductResponse>> findProductsByRestaurant(@PathVariable Long restaurantId){
		return ResponseEntity.ok(productService.findProductsByRestaurant(restaurantId));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Find product by ID", description = "Returns a product by its ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Product found"),
			@ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<ProductResponse> findProductById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.findProductById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update product", description = "Updates an existing product")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Product updated"),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
		return ResponseEntity.ok(productService.updateProduct(id, request));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete product", description = "Deletes a product by ID")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}