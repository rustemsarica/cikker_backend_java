package com.project.cikker.responses;

import java.util.List;


public class ProfileResponse {

	UserResponse user;
	List<UserResponse> followers;
	List<UserResponse> followings;
	List<PostResponse> posts;
	List<PostResponse> likes;
	
	public UserResponse getUser() {
		return user;
	}
	public void setUser(UserResponse user) {
		this.user = user;
	}
	public List<UserResponse> getFollowers() {
		return followers;
	}
	public void setFollowers(List<UserResponse> followers) {
		this.followers = followers;
	}
	public List<UserResponse> getFollowings() {
		return followings;
	}
	public void setFollowings(List<UserResponse> followings) {
		this.followings = followings;
	}
	public List<PostResponse> getPosts() {
		return posts;
	}
	public void setPosts(List<PostResponse> posts) {
		this.posts = posts;
	}
	public List<PostResponse> getLikes() {
		return likes;
	}
	public void setLikes(List<PostResponse> likes) {
		this.likes = likes;
	}
}
