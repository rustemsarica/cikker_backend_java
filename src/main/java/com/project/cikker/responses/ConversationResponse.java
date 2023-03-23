package com.project.cikker.responses;

import com.project.cikker.entities.Conversation;

public class ConversationResponse {
	Long id;
	Long sender_id;
	String sender_username;
	Long receiver_id;
	String receiver_username;
	String lastMessage;
	
	public ConversationResponse(Conversation entity, String lastMessage) {
		super();
		this.id = entity.getId();
		this.sender_id = entity.getSender().getId();
		this.sender_username = entity.getSender().getUsername();
		this.receiver_id = entity.getReceiver().getId();
		this.receiver_username = entity.getReceiver().getUsername();
		this.lastMessage = lastMessage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSender_id() {
		return sender_id;
	}

	public void setSender_id(Long sender_id) {
		this.sender_id = sender_id;
	}

	public String getSender_username() {
		return sender_username;
	}

	public void setSender_username(String sender_username) {
		this.sender_username = sender_username;
	}

	public Long getReceiver_id() {
		return receiver_id;
	}

	public void setReceiver_id(Long receiver_id) {
		this.receiver_id = receiver_id;
	}

	public String getReceiver_username() {
		return receiver_username;
	}

	public void setReceiver_username(String receiver_username) {
		this.receiver_username = receiver_username;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	
}
