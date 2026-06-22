package com.delivery_management_api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.delivery_management_api.entity.User;
import com.delivery_management_api.enums.Role;
import com.delivery_management_api.repository.UserRepository;

@Component
public class DataInitializer implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${admin.name}")
	private String adminName;

	@Value("${admin.email}")
	private String adminEmail;

	@Value("${admin.password}")
	private String adminPassword;

	@Override
	public void run(ApplicationArguments args) {
		if (userRepository.existsByEmail(adminEmail)) {
			logger.info("Admin user already exists: {}", adminEmail);
			return;
		}

		User admin = new User(adminName, adminEmail, passwordEncoder.encode(adminPassword), Role.ADMIN);
		userRepository.save(admin);
		logger.info("Default admin user created: {}", adminEmail);
	}

}