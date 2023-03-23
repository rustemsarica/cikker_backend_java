package com.project.cikker.requests;

public class SendMessageRequest {

	private Long conversation_id = null;
	
	private Long sender_id;
	
	private Long receiver_id;
	
	private String text;

	public SendMessageRequest(Long conversation_id, Long sender_id, Long receiver_id, String text) {
		super();
		this.conversation_id = conversation_id;
		this.sender_id = sender_id;
		this.receiver_id = receiver_id;
		this.text = text;
	}

	public Long getConversation_id() {
		return conversation_id;
	}

	public void setConversation_id(Long conversation_id) {
		this.conversation_id = conversation_id;
	}

	public Long getSender_id() {
		return sender_id;
	}

	public void setSender_id(Long sender_id) {
		this.sender_id = sender_id;
	}

	public Long getReceiver_id() {
		return receiver_id;
	}

	public void setReceiver_id(Long receiver_id) {
		this.receiver_id = receiver_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
