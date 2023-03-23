package com.project.cikker.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.cikker.entities.Conversation;
import com.project.cikker.entities.User;
import com.project.cikker.requests.SendMessageRequest;
import com.project.cikker.responses.ConversationResponse;
import com.project.cikker.responses.MessageResponse;
import com.project.cikker.responses.StatusMessageResponse;
import com.project.cikker.services.MessageService;
import com.project.cikker.services.UserService;

@RestController
@RequestMapping("/messages")
public class MessageController {

	private MessageService messageService;
	private UserService userService;

	public MessageController(MessageService messageService, UserService userService) {
		super();
		this.messageService = messageService;
		this.userService = userService;
	}
	
	@GetMapping("/conversations")
	public List<ConversationResponse> conversations(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.getUserByUserName(auth.getName());
		List<ConversationResponse> list = messageService.getConversationsBySenderIdOrReceiverId(user.getId());	
		
		return list;
		
	}
	
	@GetMapping("/{senderId}-{receiverId}")
	public List<MessageResponse> getAllPosts(@PathVariable Long senderId, @PathVariable Long receiverId){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.getUserByUserName(auth.getName());
		if(user==null || (user.getId()!= Long.valueOf(senderId) && user.getId()!= Long.valueOf(receiverId)) ) {
			return null;
		}
		Conversation conversation = messageService.getConversationBySenderIdAndReceiverId(senderId,receiverId);
		return messageService.getAllMessages(conversation.getId());
	}
	
	@PostMapping
	public StatusMessageResponse sendMessage(@RequestBody SendMessageRequest request) {
		Boolean send = messageService.sendMessage(request);
		StatusMessageResponse res = new StatusMessageResponse();
		if(!send) {
			res.setStatus("error");
			res.setMessage("Somethings went wrong.");
			return res;
		}
		res.setStatus("success");
		res.setMessage("Message send.");
		return res;
	}
}
