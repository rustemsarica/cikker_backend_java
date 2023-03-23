package com.project.cikker.responses;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LoginResponse {

	Long id;
	String token;
	String status;
	String username;
	String refreshToken;
	@JsonFormat(pattern="dd.MM.yyyy HH:mm")
	LocalDateTime createdAt;
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public Long getId() {
		return this.id;
	}
	public void setId(Long id2) {
		this.id=id2;		
	}
	
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username2) {
		this.username=username2;		
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getToken() {
		return this.token;
	}
	public void setToken(String token2) {
		this.token=token2;		
	}
	
	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status2) {
		this.status=status2;		
	}
}
