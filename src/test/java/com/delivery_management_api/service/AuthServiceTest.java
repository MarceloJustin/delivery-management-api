package com.delivery_management_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.delivery_management_api.dto.response.AuthResponse;
import com.delivery_management_api.dto.request.LoginRequest;
import com.delivery_management_api.dto.request.RefreshTokenRequest;
import com.delivery_management_api.dto.request.RegisterRequest;
import com.delivery_management_api.entity.RefreshToken;
import com.delivery_management_api.entity.User;
import com.delivery_management_api.enums.Role;
import com.delivery_management_api.exception.InvalidRefreshTokenException;
import com.delivery_management_api.exception.UserAlreadyExistsException;
import com.delivery_management_api.repository.UserRepository;
import com.delivery_management_api.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtService jwtService;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private RefreshTokenService refreshTokenService;

	@InjectMocks
	private AuthService authService;

	@Test
	void shouldRegisterUserSuccessfully() {
		RegisterRequest request = new RegisterRequest();
		request.setName("João");
		request.setEmail("joao@email.com");
		request.setPassword("senha123");

		User savedUser = new User("João", "joao@email.com", "$2a$10$hashedpassword", Role.CUSTOMER);
		RefreshToken savedRefreshToken = new RefreshToken("mocked-refresh-token", savedUser, Instant.now().plusSeconds(604800));

		when(userRepository.existsByEmail("joao@email.com")).thenReturn(false);
		when(passwordEncoder.encode("senha123")).thenReturn("$2a$10$hashedpassword");
		when(userRepository.save(any(User.class))).thenReturn(savedUser);
		when(jwtService.generateToken(any(User.class))).thenReturn("mocked-jwt-token");
		when(refreshTokenService.create(any(User.class))).thenReturn(savedRefreshToken);

		AuthResponse response = authService.register(request);

		assertNotNull(response);
		assertEquals("mocked-jwt-token", response.getToken());
		assertEquals("mocked-refresh-token", response.getRefreshToken());
		assertEquals("Bearer", response.getType());
		assertEquals("João", response.getName());
		assertEquals("joao@email.com", response.getEmail());
		assertEquals("CUSTOMER", response.getRole());

		verify(userRepository).existsByEmail("joao@email.com");
		verify(passwordEncoder).encode("senha123");
		verify(userRepository).save(any(User.class));
		verify(jwtService).generateToken(any(User.class));
		verify(refreshTokenService).create(any(User.class));
	}

	@Test
	void shouldThrowExceptionWhenEmailAlreadyExists() {
		RegisterRequest request = new RegisterRequest();
		request.setName("João");
		request.setEmail("joao@email.com");
		request.setPassword("senha123");

		when(userRepository.existsByEmail("joao@email.com")).thenReturn(true);

		assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));

		verify(userRepository).existsByEmail("joao@email.com");
		verify(userRepository, never()).save(any(User.class));
		verify(passwordEncoder, never()).encode(any());
		verify(jwtService, never()).generateToken(any());
	}

	@Test
	void shouldAssignCustomerRoleOnRegister() {
		RegisterRequest request = new RegisterRequest();
		request.setName("João");
		request.setEmail("joao@email.com");
		request.setPassword("senha123");

		User savedUser = new User("João", "joao@email.com", "$2a$10$hashedpassword", Role.CUSTOMER);
		RefreshToken savedRefreshToken = new RefreshToken("mocked-refresh-token", savedUser, Instant.now().plusSeconds(604800));

		when(userRepository.existsByEmail(any())).thenReturn(false);
		when(passwordEncoder.encode(any())).thenReturn("$2a$10$hashedpassword");
		when(userRepository.save(any(User.class))).thenReturn(savedUser);
		when(jwtService.generateToken(any())).thenReturn("mocked-jwt-token");
		when(refreshTokenService.create(any(User.class))).thenReturn(savedRefreshToken);

		AuthResponse response = authService.register(request);

		assertEquals("CUSTOMER", response.getRole());
	}

	@Test
	void shouldLoginSuccessfully() {
		LoginRequest request = new LoginRequest();
		request.setEmail("joao@email.com");
		request.setPassword("senha123");

		User user = new User("João", "joao@email.com", "$2a$10$hashedpassword", Role.CUSTOMER);
		RefreshToken refreshToken = new RefreshToken("mocked-refresh-token", user, Instant.now().plusSeconds(604800));

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(new UsernamePasswordAuthenticationToken("joao@email.com", null));
		when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(user));
		when(jwtService.generateToken(user)).thenReturn("mocked-jwt-token");
		when(refreshTokenService.create(user)).thenReturn(refreshToken);

		AuthResponse response = authService.login(request);

		assertNotNull(response);
		assertEquals("mocked-jwt-token", response.getToken());
		assertEquals("mocked-refresh-token", response.getRefreshToken());
		assertEquals("Bearer", response.getType());
		assertEquals("João", response.getName());
		assertEquals("joao@email.com", response.getEmail());
		assertEquals("CUSTOMER", response.getRole());

		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(userRepository).findByEmail("joao@email.com");
		verify(jwtService).generateToken(user);
		verify(refreshTokenService).create(user);
	}

	@Test
	void shouldThrowExceptionWhenCredentialsAreInvalid() {
		LoginRequest request = new LoginRequest();
		request.setEmail("joao@email.com");
		request.setPassword("senha-errada");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new BadCredentialsException("Bad credentials"));

		assertThrows(BadCredentialsException.class, () -> authService.login(request));

		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(userRepository, never()).findByEmail(any());
		verify(jwtService, never()).generateToken(any());
	}

	@Test
	void shouldRefreshTokenSuccessfully() {
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken("old-refresh-token");

		User user = new User("João", "joao@email.com", "$2a$10$hashedpassword", Role.CUSTOMER);
		RefreshToken oldToken = new RefreshToken("old-refresh-token", user, Instant.now().plusSeconds(3600));
		RefreshToken newToken = new RefreshToken("new-refresh-token", user, Instant.now().plusSeconds(3600));

		when(refreshTokenService.validate("old-refresh-token")).thenReturn(oldToken);
		when(refreshTokenService.rotate(oldToken)).thenReturn(newToken);
		when(jwtService.generateToken(user)).thenReturn("new-jwt-token");

		AuthResponse response = authService.refresh(request);

		assertNotNull(response);
		assertEquals("new-jwt-token", response.getToken());
		assertEquals("new-refresh-token", response.getRefreshToken());
		assertEquals("João", response.getName());
		assertEquals("joao@email.com", response.getEmail());
		assertEquals("CUSTOMER", response.getRole());

		verify(refreshTokenService).validate("old-refresh-token");
		verify(refreshTokenService).rotate(oldToken);
		verify(jwtService).generateToken(user);
	}

	@Test
	void shouldPropagateExceptionWhenRefreshTokenIsInvalid() {
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken("invalid-token");

		when(refreshTokenService.validate("invalid-token"))
				.thenThrow(new InvalidRefreshTokenException("Refresh token not found"));

		assertThrows(InvalidRefreshTokenException.class, () -> authService.refresh(request));

		verify(refreshTokenService, never()).rotate(any());
		verify(jwtService, never()).generateToken(any());
	}

}