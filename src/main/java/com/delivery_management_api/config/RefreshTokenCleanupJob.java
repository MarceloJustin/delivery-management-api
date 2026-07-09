package com.delivery_management_api.config;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.delivery_management_api.repository.RefreshTokenRepository;

@Component
public class RefreshTokenCleanupJob {

	private static final Logger logger = LoggerFactory.getLogger(RefreshTokenCleanupJob.class);

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Scheduled(cron = "${refresh-token.cleanup.cron}")
	public void cleanup() {
		refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
		refreshTokenRepository.deleteByRevokedTrue();
		logger.info("Refresh token cleanup executed");
	}

}