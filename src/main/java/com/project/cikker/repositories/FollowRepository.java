package com.project.cikker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.project.cikker.entities.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {

	@Query(value = 	"select followed_id from follows where user_id = :userId ", nativeQuery = true)
	List<Long> getFollowingIds(@Param("userId") Long userId);
	
	@Query(value = 	"select user_id from follows where followed_id = :userId ", nativeQuery = true)
	List<Long> getFollowerIds(@Param("userId") Long userId);
	
	@Query(value = 	"select * from follows where followed_id = :followedId and user_id = :userId ", nativeQuery = true)
	List<Follow> checkFollow(@Param("userId") Long userId,@Param("followedId") Long followedId);
	
	@Transactional
	void deleteByFollowedIdAndUserId( Long followedId,Long userId);
}
