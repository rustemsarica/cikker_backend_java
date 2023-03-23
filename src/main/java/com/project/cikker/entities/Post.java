package com.project.cikker.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="posts")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id", nullable=false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="rt_id", nullable=true)
	private Post post;

	@Column(columnDefinition="text", nullable=false)
	private String text;
	
	@CreationTimestamp
	private LocalDateTime  createdAt;	
	
	@OneToMany(mappedBy="post")
	private List<PostImage> postImages;
	
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
	
	public User getUser() {
		return this.user;
	}
	public void setUser(User user2) {
		this.user=user2;		
	}
	
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	
	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt2) {
		this.createdAt=createdAt2;		
	}
	public List<PostImage> getImages() {
		return postImages;
	}
	public void setImages(List<PostImage> images) {
		this.postImages = images;
	}
}
