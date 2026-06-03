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

import com.delivery_management_api.dto.CreateRestaurantRequest;
import com.delivery_management_api.dto.ErrorResponse;
import com.delivery_management_api.dto.RestaurantResponse;
import com.delivery_management_api.dto.UpdateRestaurantRequest;
import com.delivery_management_api.dto.ValidationErrorResponse;
import com.delivery_management_api.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Restaurants", description = "Operations related to restaurant management")
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;

	@PostMapping("/api/restaurants")
	@Operation(summary = "Create restaurant", description = "Creates a new restaurant")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Restaurant created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))) })
	public RestaurantResponse creatRestaurant(@Valid @RequestBody CreateRestaurantRequest request) {
		return restaurantService.createRestaurant(request);
	}

	@GetMapping("/api/restaurants")
	@Operation(summary = "List restaurants", description = "Returns all registered restaurants")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Restaurants found") })
	public List<RestaurantResponse> findAllRestaurant() {
		return restaurantService.findAllRestaurants();
	}

	@GetMapping("/api/restaurants/{id}")
	@Operation(summary = "Find restaurant by ID", description = "Returns a restaurant by its ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Restaurant found"),
			@ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public RestaurantResponse findRestaurantById(@PathVariable Long id) {
		return restaurantService.findRestaurantById(id);
	}

	@PutMapping("/api/restaurants/{id}")
	@Operation(summary = "Update restaurant", description = "Updates an existing restaurant")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Restaurant updated"),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public RestaurantResponse updateRestaurant(@PathVariable Long id,
			@Valid @RequestBody UpdateRestaurantRequest request) {
		return restaurantService.updateRestaurant(id, request);
	}

	@DeleteMapping("/api/restaurants/{id}")
	@Operation(summary = "Delete restaurant", description = "Deletes a restaurant by ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Restaurant deleted"),
			@ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	public void deleteRestaurant(@PathVariable Long id) {
		restaurantService.deleteRestaurant(id);
	}
}