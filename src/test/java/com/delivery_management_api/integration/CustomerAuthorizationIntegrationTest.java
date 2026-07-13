package com.delivery_management_api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.delivery_management_api.entity.Customer;
import com.delivery_management_api.entity.User;
import com.delivery_management_api.repository.CustomerRepository;
import com.delivery_management_api.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CustomerAuthorizationIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	void customerCanViewOwnProfile() throws Exception {
		String tokenA = registerAndGetToken("joaoA@email.com");
		Long customerIdA = findCustomerId("joaoA@email.com");

		mockMvc.perform(get("/api/customers/" + customerIdA).header("Authorization", "Bearer " + tokenA))
				.andExpect(status().isOk());
	}

	@Test
	void customerCannotViewAnotherProfile() throws Exception {
		String tokenA = registerAndGetToken("joaoA@email.com");
		registerAndGetToken("joaoB@email.com");
		Long customerIdB = findCustomerId("joaoB@email.com");

		mockMvc.perform(get("/api/customers/" + customerIdB).header("Authorization", "Bearer " + tokenA))
				.andExpect(status().isForbidden());
	}

	@Test
	void customerCanUpdateOwnProfile() throws Exception {
		String tokenA = registerAndGetToken("joaoA@email.com");
		Long customerIdA = findCustomerId("joaoA@email.com");

		String body = """
				{
				  "name": "João Atualizado",
				  "email": "joaoA@email.com"
				}
				""";

		mockMvc.perform(put("/api/customers/" + customerIdA)
				.header("Authorization", "Bearer " + tokenA)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isOk());
	}

	@Test
	void customerCannotUpdateAnotherProfile() throws Exception {
		String tokenA = registerAndGetToken("joaoA@email.com");
		registerAndGetToken("joaoB@email.com");
		Long customerIdB = findCustomerId("joaoB@email.com");

		String body = """
				{
				  "name": "Hackeado",
				  "email": "joaoB@email.com"
				}
				""";

		mockMvc.perform(put("/api/customers/" + customerIdB)
				.header("Authorization", "Bearer " + tokenA)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isForbidden());
	}

	@Test
	void customerCannotListAllCustomers() throws Exception {
		String tokenA = registerAndGetToken("joaoA@email.com");

		mockMvc.perform(get("/api/customers").header("Authorization", "Bearer " + tokenA))
				.andExpect(status().isForbidden());
	}

	@Test
	void customerCannotCreateCustomer() throws Exception {
		String tokenA = registerAndGetToken("joaoA@email.com");

		String body = """
				{
				  "name": "Novo Cliente",
				  "email": "novo@email.com"
				}
				""";

		mockMvc.perform(post("/api/customers")
				.header("Authorization", "Bearer " + tokenA)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isForbidden());
	}

	private String registerAndGetToken(String email) throws Exception {
		String body = """
				{
				  "name": "Teste",
				  "email": "%s",
				  "password": "senha123"
				}
				""".formatted(email);

		MvcResult result = mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isCreated())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		return responseBody.split("\"token\":\"")[1].split("\"")[0];
	}

	private Long findCustomerId(String email) {
		User user = userRepository.findByEmail(email).orElseThrow();
		Customer customer = customerRepository.findByUser(user).orElseThrow();
		return customer.getId();
	}
}