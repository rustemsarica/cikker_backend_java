package com.project.cikker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.cikker.entities.Message;

public interface MessageRepository extends JpaRepository<Message,Long> {

	List<Message> getByConversationId(Long conversationId);

}
