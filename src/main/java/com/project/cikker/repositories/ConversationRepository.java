package com.project.cikker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.cikker.entities.Conversation;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

	Conversation findBySenderIdAndReceiverId(Long senderId, Long receiverId);
	
	@Query(value = "select * from conversations where receiver_id=:userId or sender_id=:userId order by id desc", 
			nativeQuery = true)
	List<Conversation> getConversationsBySenderIdOrReceiverId(@Param("userId") Long id);

}
