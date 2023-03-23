package com.project.cikker.repositories;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.project.cikker.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findByUserId(Long userId);
	List<Post> findAllByOrderByIdDesc();
	List<Post> findByUserIdOrderByIdDesc(Long userId);
	
	@Transactional
	List<Post> getPostByIdIn(List<Long> ids);
	
	List<Post> findAllByCreatedAtAfter(LocalDateTime startDate);
	List<Post> findAllByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
	
	List<Post> findAllByTextContaining(String hashtag);
}
