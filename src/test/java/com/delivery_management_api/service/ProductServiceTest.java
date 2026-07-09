package com.delivery_management_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private RestaurantRepository restaurantRepository;

	@Spy
	private ProductMapper productMapper = new ProductMapper();

	@InjectMocks
	private ProductService productService;
	
	@Test
	void shoulThrowExceptionWhenProducDoesNotExist() {
	
		when(productRepository.findById(999L)).thenReturn(Optional.empty());
		
		assertThrows(ProductNotFoundException.class, () -> productService.findProductById(999L));
	}
	
	@Test
	void shouldDeleteProductWhenIdExists() {
		Restaurant restaurant = new Restaurant("Burger House", "Hamburgueria", BigDecimal.valueOf(5.00));
		
		Product product = new Product("X-Burger", BigDecimal.valueOf(25.90), restaurant);
		
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		
		productService.deleteProduct(1L);
		
		verify(productRepository).deleteById(1L);
	}
	
	@Test
	void shouldThrowExceptionWhenDeletingNonExistentProduct() {
		
		when(productRepository.findById(999L)).thenReturn(Optional.empty());
		
		assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(999L));
		
		verify(productRepository, never()).deleteById(anyLong());
	}
	
	@Test
	void shouldCreateProductSuccessfully() {
		
		Restaurant restaurant = new Restaurant("Pizza do João", "Pizzaria", BigDecimal.valueOf(5.00));
		
		CreateProductRequest request = new CreateProductRequest();
		request.setName("Pizza 4Queijos");
		request.setPrice(BigDecimal.valueOf(15.00));
		request.setRestaurantId(1L);
		
		Product product = new Product("Pizza 4Queijos", BigDecimal.valueOf(15.00), restaurant);
		
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

		when(productRepository.save(any(Product.class))).thenReturn(product);

		ProductResponse response = productService.createProduct(request);

		assertEquals("Pizza 4Queijos", response.getName());

		assertEquals(BigDecimal.valueOf(15.00), response.getPrice());

		verify(productRepository).save(any(Product.class));

		verify(restaurantRepository).findById(1L);
	}
	
	@Test
	void shouldThrowExceptionWhenRestaurantDoesNotExist() {
		
		CreateProductRequest request = new CreateProductRequest();
		request.setName("Pizza 4Queijos");
		request.setPrice(BigDecimal.valueOf(15.00));
		request.setRestaurantId(999L);
		
		when(restaurantRepository.findById(999L)).thenReturn(Optional.empty());
		
		assertThrows(RestaurantNotFoundException.class, () -> productService.createProduct(request));
		
		verify(productRepository, never()).save(any(Product.class));
		
		verify(restaurantRepository).findById(999L);
		
	}
	
	@Test
	void shouldUpdateProductSuccessfully() {
		
		Restaurant restaurant = new Restaurant("Pizza do João", "Pizzaria", BigDecimal.valueOf(5.00));
		
		UpdateProductRequest request = new UpdateProductRequest();
		request.setName("Pizza 4Queijos");
		request.setPrice(BigDecimal.valueOf(15.00));
		request.setRestaurantId(1L);
		
		Product product = new Product("Pizza 4Queijos", BigDecimal.valueOf(15.00), restaurant);
		
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

		when(productRepository.save(any(Product.class))).thenReturn(product);

		ProductResponse response = productService.updateProduct(1L, request);
		
		assertEquals("Pizza 4Queijos", response.getName());
		
		assertEquals(BigDecimal.valueOf(15.00), response.getPrice());
		
		verify(productRepository).findById(1L);
		
		verify(productRepository).save(any(Product.class));
		
		verify(restaurantRepository).findById(1L);
		
	}
	
	@Test
	void shouldThrowExceptionWhenUpdatingNonExistentProduct() {
		
		UpdateProductRequest request = new UpdateProductRequest();
		request.setName("Pizza 4Queijos");
		request.setPrice(BigDecimal.valueOf(15.00));
		request.setRestaurantId(1L);
		
		when(productRepository.findById(999L)).thenReturn(Optional.empty());
		
		assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(999L, request));
		
		verify(productRepository).findById(999L);
		
		verify(productRepository, never()).save(any(Product.class));
		
		verify(restaurantRepository, never()).findById(anyLong());
		
	}
}