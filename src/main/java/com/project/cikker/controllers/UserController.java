package com.project.cikker.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.cikker.entities.User;
import com.project.cikker.responses.ProfileResponse;
import com.project.cikker.responses.StatusMessageResponse;
import com.project.cikker.responses.UserResponse;
import com.project.cikker.services.FollowService;
import com.project.cikker.services.LikeService;
import com.project.cikker.services.PostService;
import com.project.cikker.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private UserService userService;
	private FollowService followService;
	private PostService postService;
	private LikeService likeService;
	
	public UserController(UserService userService, FollowService followService, PostService postService, LikeService likeService) {
		this.userService = userService;
		this.followService = followService;
		this.postService = postService;
		this.likeService = likeService;
	}
	
	@GetMapping
	public List<User> getAllUsers(){
		return userService.getAllUsers();
	}
	
	@PostMapping
	public User createUser(@RequestBody User newUser) {
		return userService.save(newUser);
	}
	
	@GetMapping("/{userId}")
	public User getUser(@PathVariable Long userId){
		return userService.getUser(userId);
	}
	
	@PutMapping("/{userId}")
	public User updateUser(@PathVariable Long userId, @RequestBody User newUser){
		return userService.updateUser(userId, newUser);
	}
	
	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable Long userId){
		userService.deleteUser(userId);
	}
	
	@PutMapping("/{userId}/avatar")
	public StatusMessageResponse updateUser(@PathVariable Long userId, @RequestParam("file") MultipartFile file){
		return userService.updateUserAvatar(userId, file);
	}
	
	
	@GetMapping("/username/{username}")
	public ProfileResponse userProfile(@PathVariable String username){
		User user = userService.getUserByUserName(username);
		
		ProfileResponse res = new ProfileResponse();
		if(user==null) {
			return res;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		String currentUserName = auth.getName();
		User userAuth = userService.getUserByUserName(currentUserName);
		
		UserResponse userRes = new UserResponse(user.getId(),user.getUsername(),followService.checkFollow(userAuth.getId(), user.getId()));
		
		
		
		res.setUser(userRes);
		res.setFollowings(followService.followings(user.getId()));
		res.setFollowers(followService.followers(user.getId()));
		res.setPosts(postService.getUserPosts(user.getId()));
		res.setLikes(likeService.likedPost(user.getId()));
		return res;
	}
}
