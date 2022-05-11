package com.example.mkksecurity.service;

import com.example.mkksecurity.aop.ExecutionTime;
import com.example.mkksecurity.models.RefreshToken;
import com.example.mkksecurity.repository.RefreshTokenRepository;
import com.example.mkksecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

	@Value("${mkk.app.jwtRefrehExpritationMs}")
	private Long refreshTokenDurationMs;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

	@ExecutionTime
	public RefreshToken createRefreshToken(Long userId) {

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUser(userRepository.getById(userId));
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;

	}

	public RefreshToken findByToken(String token) {
		return refreshTokenRepository.findByToken(token).get();
	}

	@ExecutionTime
	@Transactional
	public RefreshToken verifyExpritation(RefreshToken refreshToken){
		if(refreshToken.getExpiryDate().compareTo(Instant.now())<0) {
			refreshTokenRepository.delete(refreshToken);
		}
		return refreshToken;
	}


}
