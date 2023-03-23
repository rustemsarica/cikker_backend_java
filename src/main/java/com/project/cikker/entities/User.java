package com.project.cikker.entities;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	@JsonIgnore
	private String password;
	
	
	private String info;
	
	@CreationTimestamp
	private LocalDateTime  createdAt;
	
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
	
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password2) {
		this.password=password2;
	}
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
