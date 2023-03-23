package com.project.cikker.services;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.cikker.entities.RefreshToken;
import com.project.cikker.entities.User;
import com.project.cikker.repositories.RefreshTokenRepository;

@Service
public class RefreshTokenService {

	@Value("${refresh.token.expires}")
	Long expireSeconds;
	
	private RefreshTokenRepository refreshTokenRepository;

	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
		super();
		this.refreshTokenRepository = refreshTokenRepository;
	}
	
	public String createRefreshToken(User user) {
		RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
		if(token == null) {
			token =	new RefreshToken();
			token.setUser(user);
		}
		token.setToken(UUID.randomUUID().toString());
		token.setExpiryDate(Date.from(Instant.now().plusSeconds(expireSeconds)));
		refreshTokenRepository.save(token);
		return token.getToken();
	}
	
	public boolean isRefreshExpired(RefreshToken token) {
		 return token.getExpiryDate().before(new Date());
	} 

	public RefreshToken getByUser(Long userId) {
		return refreshTokenRepository.findByUserId(userId);		
	}
	
	
	
}
