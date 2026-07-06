package com.delivery_management_api.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.delivery_management_api.entity.RefreshToken;
import com.delivery_management_api.entity.User;
import com.delivery_management_api.exception.InvalidRefreshTokenException;
import com.delivery_management_api.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Value("${refresh-token.expiration}")
	private long expiration;

	public RefreshToken create(User user) {
		RefreshToken refreshToken = new RefreshToken(
				UUID.randomUUID().toString(),
				user,
				Instant.now().plusMillis(expiration)
		);

		return refreshTokenRepository.save(refreshToken);
	}

	public RefreshToken validate(String token) {
		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new InvalidRefreshTokenException("Refresh token not found"));

		if (refreshToken.isRevoked()) {
			throw new InvalidRefreshTokenException("Refresh token has been revoked");
		}

		if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
			throw new InvalidRefreshTokenException("Refresh token has expired");
		}

		return refreshToken;
	}

	public RefreshToken rotate(RefreshToken oldToken) {
		oldToken.setRevoked(true);
		refreshTokenRepository.save(oldToken);

		return create(oldToken.getUser());
	}

}