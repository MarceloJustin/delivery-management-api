package com.delivery_management_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.delivery_management_api.dto.AuthResponse;
import com.delivery_management_api.dto.request.LoginRequest;
import com.delivery_management_api.dto.request.RefreshTokenRequest;
import com.delivery_management_api.dto.request.RegisterRequest;
import com.delivery_management_api.entity.RefreshToken;
import com.delivery_management_api.entity.User;
import com.delivery_management_api.enums.Role;
import com.delivery_management_api.exception.UserAlreadyExistsException;
import com.delivery_management_api.repository.UserRepository;
import com.delivery_management_api.security.JwtService;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RefreshTokenService refreshTokenService;

	public AuthResponse register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new UserAlreadyExistsException(request.getEmail());
		}

		User user = new User(
				request.getName(),
				request.getEmail(),
				passwordEncoder.encode(request.getPassword()),
				Role.CUSTOMER
		);

		userRepository.save(user);

		String token = jwtService.generateToken(user);
		RefreshToken refreshToken = refreshTokenService.create(user);

		return new AuthResponse(token, refreshToken.getToken(), user.getName(), user.getEmail(), user.getRole().name());
	}

	public AuthResponse login(LoginRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
		);

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow();

		String token = jwtService.generateToken(user);
		RefreshToken refreshToken = refreshTokenService.create(user);

		return new AuthResponse(token, refreshToken.getToken(), user.getName(), user.getEmail(), user.getRole().name());
	}

	public AuthResponse refresh(RefreshTokenRequest request) {
		RefreshToken oldToken = refreshTokenService.validate(request.getRefreshToken());
		RefreshToken newToken = refreshTokenService.rotate(oldToken);

		User user = oldToken.getUser();
		String token = jwtService.generateToken(user);

		return new AuthResponse(token, newToken.getToken(), user.getName(), user.getEmail(), user.getRole().name());
	}
}