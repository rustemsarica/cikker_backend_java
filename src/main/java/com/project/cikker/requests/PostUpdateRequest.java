package com.project.cikker.requests;

public class PostUpdateRequest {

	Long id;
	String text;
	Long userId;
	
	public Long getId() {
		return this.id;
	}
	public void setId(Long id2) {
		this.id=id2;		
	}
	
	public String getText() {
		return this.text;
	}
	public void setText(String text2) {
		this.text=text2;		
	}
	
	public Long getUserId() {
		return this.userId;
	}
	public void setUserId(Long userId2) {
		this.userId=userId2;
	}
}
