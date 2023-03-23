package com.project.cikker.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.cikker.entities.Conversation;
import com.project.cikker.entities.Message;
import com.project.cikker.entities.User;
import com.project.cikker.repositories.ConversationRepository;
import com.project.cikker.repositories.MessageRepository;
import com.project.cikker.requests.SendMessageRequest;
import com.project.cikker.responses.ConversationResponse;
import com.project.cikker.responses.MessageResponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class MessageService {

	@PersistenceContext
    private EntityManager entityManager;
	private MessageRepository messageRepository;
	private ConversationRepository conversationRepository;
	private UserService userService;
	
	public MessageService(MessageRepository messageRepository, ConversationRepository conversationRepository, UserService userService) {
		super();
		this.messageRepository = messageRepository;
		this.conversationRepository = conversationRepository;
		this.userService = userService;
	}

	public List<MessageResponse> getAllMessages(Long conversationId) {
		List<Message> list = messageRepository.getByConversationId(conversationId);
		return list.stream().map(item->{
			return new MessageResponse(item);
		}).collect(Collectors.toList());
	}

	public Conversation getConversationBySenderIdAndReceiverId(Long senderId, Long receiverId) {
		// TODO Auto-generated method stub
		return conversationRepository.findBySenderIdAndReceiverId(senderId,receiverId);
	}
	
	public Boolean sendMessage(SendMessageRequest request) {
		Long conversation_id=request.getConversation_id();
		Conversation conversation;
		System.out.println("id ="+conversation_id);
		if(conversation_id==null) {
			Conversation conv = new Conversation();
			conv.setSender(userService.getUser(request.getSender_id()));
			conv.setReceiver(userService.getUser(request.getReceiver_id()));
			Conversation saved = conversationRepository.save(conv);
			conversation=saved;
			conversation_id=saved.getId();
		}else {
			conversation = conversationRepository.findById(conversation_id).orElse(null);
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.getUserByUserName(auth.getName());
		Message message = new Message();
		message.setConversation(conversation);
		message.setSender(user);
		message.setText(request.getText());
		Message saved = messageRepository.save(message);
		if(saved==null) {
			return false;
		}
		return true;
	}

	public List<ConversationResponse> getConversationsBySenderIdOrReceiverId(Long id) {
		List<Conversation> list =  conversationRepository.getConversationsBySenderIdOrReceiverId(id);
		
		return list.stream().map(p -> { 			
			List<Message> message = entityManager.createQuery("SELECT m FROM Message m WHERE m.conversation="+p.getId()+" ORDER BY m.createdAt desc", Message.class).setMaxResults(1).getResultList();
			return new ConversationResponse(p, message.get(0).getText());}).collect(Collectors.toList());
		
	}

	
}
