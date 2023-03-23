package com.project.cikker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.cikker.entities.PostLike;

public interface LikeRepository extends JpaRepository<PostLike, Long> {

	List<PostLike> findByUserIdAndPostId(Long userId, Long postId);

	List<PostLike> findByUserId(Long userId);

	List<PostLike> findByPostId(Long postId);

	@Query(value = 	"select 'liked', l.post_id, u.avatar, u.username from "
			+ "post_like l left join user u on u.id = l.user_id "
			+ "where l.post_id in :postIds limit 5", nativeQuery = true)
	List<Object> findUserLikesByPostId(@Param("postIds") List<Long> postIds);
	
	@Query(value = 	"select post_id from post_like where user_id = :userId", nativeQuery = true)
	List<Long> getPostIdsByUserId(@Param("userId") Long userId);
}
