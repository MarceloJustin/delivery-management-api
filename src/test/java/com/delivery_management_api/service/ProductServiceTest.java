package com.delivery_management_api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.mockito.junit.jupiter.MockitoExtension;

import com.delivery_management_api.entity.Product;
import com.delivery_management_api.entity.Restaurant;
import com.delivery_management_api.exception.ProductNotFoundException;
import com.delivery_management_api.repository.ProductRepository;
import com.delivery_management_api.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private RestaurantRepository restaurantRepository;
	
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
}













