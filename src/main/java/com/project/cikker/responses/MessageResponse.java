package com.project.cikker.responses;

import java.time.LocalDateTime;

import com.project.cikker.entities.Message;
import com.project.cikker.entities.User;

public class MessageResponse {
	
	private Long id;
	private String text;
	private User user;
	private LocalDateTime  createdAt;
	
	public MessageResponse(Message message) {
		super();
		this.id = message.getId();
		this.text = message.getText();
		this.user = message.getSender();
		this.createdAt = message.getCreatedAt();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
