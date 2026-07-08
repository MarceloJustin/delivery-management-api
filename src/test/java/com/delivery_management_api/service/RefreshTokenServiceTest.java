package com.delivery_management_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.delivery_management_api.entity.RefreshToken;
import com.delivery_management_api.entity.User;
import com.delivery_management_api.enums.Role;
import com.delivery_management_api.exception.InvalidRefreshTokenException;
import com.delivery_management_api.repository.RefreshTokenRepository;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@InjectMocks
	private RefreshTokenService refreshTokenService;

	private User user;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(refreshTokenService, "expiration", 604800000L);
		user = new User("João", "joao@email.com", "$2a$10$hashedpassword", Role.CUSTOMER);
	}

	@Test
	void shouldCreateRefreshTokenSuccessfully() {
		when(refreshTokenRepository.save(any(RefreshToken.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		RefreshToken refreshToken = refreshTokenService.create(user);

		assertNotNull(refreshToken.getToken());
		assertFalse(refreshToken.isRevoked());
		assertTrue(refreshToken.getExpiryDate().isAfter(Instant.now()));
		assertEquals(user, refreshToken.getUser());

		verify(refreshTokenRepository).save(any(RefreshToken.class));
	}

	@Test
	void shouldValidateTokenSuccessfully() {
		RefreshToken token = new RefreshToken("valid-token", user, Instant.now().plusSeconds(3600));

		when(refreshTokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));

		RefreshToken result = refreshTokenService.validate("valid-token");

		assertEquals(token, result);
	}

	@Test
	void shouldThrowWhenTokenNotFound() {
		when(refreshTokenRepository.findByToken("unknown-token")).thenReturn(Optional.empty());

		assertThrows(InvalidRefreshTokenException.class, () -> refreshTokenService.validate("unknown-token"));
	}

	@Test
	void shouldThrowWhenTokenIsRevoked() {
		RefreshToken token = new RefreshToken("revoked-token", user, Instant.now().plusSeconds(3600));
		token.setRevoked(true);

		when(refreshTokenRepository.findByToken("revoked-token")).thenReturn(Optional.of(token));

		assertThrows(InvalidRefreshTokenException.class, () -> refreshTokenService.validate("revoked-token"));
	}

	@Test
	void shouldThrowWhenTokenIsExpired() {
		RefreshToken token = new RefreshToken("expired-token", user, Instant.now().minusSeconds(3600));

		when(refreshTokenRepository.findByToken("expired-token")).thenReturn(Optional.of(token));

		assertThrows(InvalidRefreshTokenException.class, () -> refreshTokenService.validate("expired-token"));
	}

	@Test
	void shouldRotateTokenSuccessfully() {
		RefreshToken oldToken = new RefreshToken("old-token", user, Instant.now().plusSeconds(3600));

		when(refreshTokenRepository.save(any(RefreshToken.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		RefreshToken newToken = refreshTokenService.rotate(oldToken);

		assertTrue(oldToken.isRevoked());
		assertNotNull(newToken.getToken());
		assertNotEquals(oldToken.getToken(), newToken.getToken());
		assertEquals(user, newToken.getUser());

		verify(refreshTokenRepository, times(2)).save(any(RefreshToken.class));
	}

}