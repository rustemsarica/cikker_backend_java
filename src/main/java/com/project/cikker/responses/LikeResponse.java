package com.project.cikker.responses;

import com.project.cikker.entities.PostLike;

public class LikeResponse {

	Long id;
	Long userId;
	Long postId;
	
	public LikeResponse(PostLike entity) {
		this.id = entity.getId();
		this.userId = entity.getUser().getId();
		this.postId = entity.getPost().getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	} 
}
