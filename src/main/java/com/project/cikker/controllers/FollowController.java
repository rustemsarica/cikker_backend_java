package com.project.cikker.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.cikker.requests.FollowCreateRequest;
import com.project.cikker.responses.StatusMessageResponse;
import com.project.cikker.responses.UserResponse;
import com.project.cikker.services.FollowService;

@RestController
@RequestMapping("/follows")
public class FollowController {

	private FollowService followService;

	public FollowController(FollowService followService) {
		super();
		this.followService = followService;
	}

	@PostMapping
	public StatusMessageResponse follow(@RequestBody FollowCreateRequest request) {
		followService.follow(request);
		StatusMessageResponse res = new StatusMessageResponse();
		res.setStatus("success");
		res.setMessage("User followed.");
		return res;
	}
	
	@PostMapping("/unfollow")
	public StatusMessageResponse unfollow(@RequestBody FollowCreateRequest request) {
		followService.unfollow(request.getUserId(), request.getFollowId());
		StatusMessageResponse res = new StatusMessageResponse();
		res.setStatus("success");
		res.setMessage("User unfollowed.");
		return res;
	}
	
	@GetMapping("/{userId}/followings")
	public List<UserResponse> followings(@PathVariable Long userId){
		return followService.followings(userId);
	}
	
	@GetMapping("/{userId}/followers")
	public List<UserResponse> followers(@PathVariable Long userId){
		return followService.followers(userId);
	}
	
	@GetMapping("/{userId}/suggests")
	public List<UserResponse> suggests(@PathVariable Long userId){ 
		return followService.suggests(userId);
	}
}
