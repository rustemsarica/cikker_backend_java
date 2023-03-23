package com.project.cikker.responses;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.cikker.entities.Post;
import com.project.cikker.entities.PostImage;

public class PostResponse {
	
	private Long id;
	private Long userId;
	private String username;
	private String text;
	private String avatar;
	private List<LikeResponse> likes;
	private Post rt;
	private Boolean followed;
	@JsonFormat(pattern="dd.MM.yyyy HH:mm")
	private LocalDateTime createdAt;
	@JsonFormat(pattern="dd.MM.yyyy HH:mm")
	private LocalDateTime updatedAt;
	private List<PostImage> images;
	
	public PostResponse(Post entity, List<LikeResponse> likes, Boolean followed) {

		this.id = entity.getId();
		this.text = entity.getText();
		this.userId = entity.getUser().getId();
		this.username = entity.getUser().getUsername();
		this.createdAt = entity.getCreatedAt();
		this.rt = entity.getPost();
		this.likes = likes;
		this.followed = followed;
		this.images = entity.getImages();
	}
	
	public Long getId() {
		return this.id;
	}
	public void setId(Long id2) {
		this.id=id2;		
	}
	
	public Post getRt() {
		return rt;
	}

	public void setRt(Post rt) {
		this.rt = rt;
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
	public void setUserId(Long userId) {
		this.userId=userId;		
	}
	
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username=username;		
	}
	
	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt2) {
		this.createdAt=createdAt2;		
	}
	
	public LocalDateTime getUpdatedAt() {
		return this.updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt2) {
		this.updatedAt=updatedAt2;		
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<LikeResponse> getLikes() {
		return likes;
	}

	public void setLikes(List<LikeResponse> likes) {
		this.likes = likes;
	}

	public Boolean getFollowed() {
		return followed;
	}

	public void setFollowed(Boolean followed) {
		this.followed = followed;
	}

	public List<PostImage> getImages() {
		return images;
	}

	public void setImages(List<PostImage> images) {
		this.images = images;
	}
}
