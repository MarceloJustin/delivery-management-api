package com.delivery_management_api.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.delivery_management_api.entity.Customer;
import com.delivery_management_api.repository.CustomerRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CustomerControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	void shouldCreateCustomer() throws Exception {
		String requestBody = """
				{
				  "name": "Marcelo",
				  "email": "marcelo@email.com"
				}
				""";

		mockMvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.name").value("Marcelo"))
				.andExpect(jsonPath("$.email").value("marcelo@email.com"));

		Customer customerCreated = customerRepository.findAll().getFirst();

		assertNotNull(customerCreated.getId());
		assertEquals("Marcelo", customerCreated.getName());
		assertEquals("marcelo@email.com", customerCreated.getEmail());
	}

	@Test
	void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
		String requestBody = """
				{
				  "name": " ",
				  "email": "marcelo@email.com"
				}
				""";

		mockMvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
		String requestBody = """
				{
				  "name": "Marcelo",
				  "email": "email-invalido"
				}
				""";

		mockMvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnCustomerList() throws Exception {
		createCustomer();

		customerRepository.save(new Customer("João", "joao@email.com"));

		mockMvc.perform(get("/api/customers")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(2)).andExpect(jsonPath("$.totalElements").value(2));
	}

	@Test
	void shouldReturnCustomerWhenExists() throws Exception {
		Customer customer = createCustomer();

		mockMvc.perform(get("/api/customers/" + customer.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(customer.getId())).andExpect(jsonPath("$.name").value("Marcelo"))
				.andExpect(jsonPath("$.email").value("marcelo@email.com"));
	}

	@Test
	void shouldReturnNotFoundWhenCustomerDoesNotExist() throws Exception {
		mockMvc.perform(get("/api/customers/999")).andExpect(status().isNotFound());
	}

	@Test
	void shouldUpdateCustomer() throws Exception {
		Customer customer = createCustomer();

		String requestBody = """
				{
				  "name": "Marcelo Justin",
				  "email": "marcelojustin@email.com"
				}
				""";

		mockMvc.perform(put("/api/customers/" + customer.getId()).contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Marcelo Justin"))
				.andExpect(jsonPath("$.email").value("marcelojustin@email.com"));

		Customer updated = customerRepository.findById(customer.getId()).orElseThrow();

		assertEquals("Marcelo Justin", updated.getName());
		assertEquals("marcelojustin@email.com", updated.getEmail());
	}

	@Test
	void shouldReturnNotFoundWhenUpdatingNonExistingCustomer() throws Exception {
		String requestBody = """
				{
				  "name": "Marcelo Justin",
				  "email": "marcelojustin@email.com"
				}
				""";

		mockMvc.perform(put("/api/customers/999").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldDeleteCustomer() throws Exception {
		Customer customer = createCustomer();

		mockMvc.perform(delete("/api/customers/" + customer.getId())).andExpect(status().isNoContent());

		assertFalse(customerRepository.findById(customer.getId()).isPresent());
	}

	@Test
	void shouldReturnNotFoundWhenDeletingNonExistingCustomer() throws Exception {
		mockMvc.perform(delete("/api/customers/999")).andExpect(status().isNotFound());
	}

	private Customer createCustomer() {
		return customerRepository.save(new Customer("Marcelo", "marcelo@email.com"));
	}
}