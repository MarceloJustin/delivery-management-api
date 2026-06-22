package com.delivery_management_api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.delivery_management_api.entity.User;
import com.delivery_management_api.enums.Role;
import com.delivery_management_api.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void shouldRegisterUserSuccessfully() throws Exception {
		String requestBody = """
				{
				  "name": "João da Silva",
				  "email": "joao@email.com",
				  "password": "senha123"
				}
				""";

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.token").exists())
				.andExpect(jsonPath("$.type").value("Bearer"))
				.andExpect(jsonPath("$.name").value("João da Silva"))
				.andExpect(jsonPath("$.email").value("joao@email.com"))
				.andExpect(jsonPath("$.role").value("CUSTOMER"));
	}

	@Test
	void shouldReturnBadRequestWhenRegisterFieldsAreInvalid() throws Exception {
		String requestBody = """
				{
				  "name": "",
				  "email": "email-invalido",
				  "password": "123"
				}
				""";

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
		userRepository.save(new User("João", "joao@email.com", passwordEncoder.encode("senha123"), Role.CUSTOMER));

		String requestBody = """
				{
				  "name": "João da Silva",
				  "email": "joao@email.com",
				  "password": "senha123"
				}
				""";

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isConflict());
	}

	@Test
	void shouldLoginSuccessfully() throws Exception {
		userRepository.save(new User("João", "joao@email.com", passwordEncoder.encode("senha123"), Role.CUSTOMER));

		String requestBody = """
				{
				  "email": "joao@email.com",
				  "password": "senha123"
				}
				""";

		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andExpect(jsonPath("$.type").value("Bearer"))
				.andExpect(jsonPath("$.name").value("João"))
				.andExpect(jsonPath("$.email").value("joao@email.com"))
				.andExpect(jsonPath("$.role").value("CUSTOMER"));
	}

	@Test
	void shouldReturnBadRequestWhenLoginFieldsAreInvalid() throws Exception {
		String requestBody = """
				{
				  "email": "email-invalido",
				  "password": ""
				}
				""";

		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnUnauthorizedWhenCredentialsAreInvalid() throws Exception {
		userRepository.save(new User("João", "joao@email.com", passwordEncoder.encode("senha123"), Role.CUSTOMER));

		String requestBody = """
				{
				  "email": "joao@email.com",
				  "password": "senha-errada"
				}
				""";

		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void shouldReturnUnauthorizedWhenAccessingProtectedEndpointWithoutToken() throws Exception {
		mockMvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "name": "João", "email": "joao@email.com" }
						"""))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void shouldReturnForbiddenWhenCustomerAccessesAdminEndpoint() throws Exception {
		userRepository.save(new User("João", "joao@email.com", passwordEncoder.encode("senha123"), Role.CUSTOMER));

		String token = loginAndGetToken("joao@email.com", "senha123");

		String requestBody = """
				{
				  "name": "Burger King",
				  "category": "Fast Food",
				  "deliveryFee": 9.9
				}
				""";

		mockMvc.perform(post("/api/restaurants")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody)
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isForbidden());
	}

	@Test
	void shouldAllowAdminToAccessAdminEndpoint() throws Exception {
		userRepository.save(new User("Admin", "admin@email.com", passwordEncoder.encode("senha123"), Role.ADMIN));

		String token = loginAndGetToken("admin@email.com", "senha123");

		String requestBody = """
				{
				  "name": "Burger King",
				  "category": "Fast Food",
				  "deliveryFee": 9.9
				}
				""";

		mockMvc.perform(post("/api/restaurants")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody)
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isCreated());
	}

	private String loginAndGetToken(String email, String password) throws Exception {
		String loginBody = """
				{
				  "email": "%s",
				  "password": "%s"
				}
				""".formatted(email, password);

		MvcResult result = mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginBody))
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		return responseBody.split("\"token\":\"")[1].split("\"")[0];
	}

}