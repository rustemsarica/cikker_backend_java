package com.project.cikker.responses;

public class UserResponse {
	Long id;
	String username;
	Boolean followed=false;
	
	public UserResponse(Long id, String username, Boolean followed) {
		super();
		this.id = id;
		this.username = username;
		this.followed = followed;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Boolean getFollowed() {
		return followed;
	}
	public void setFollowed(Boolean followed) {
		this.followed = followed;
	}
	
}
