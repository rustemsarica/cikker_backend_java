package com.project.cikker.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.cikker.entities.Follow;
import com.project.cikker.entities.User;
import com.project.cikker.repositories.FollowRepository;
import com.project.cikker.requests.FollowCreateRequest;
import com.project.cikker.responses.UserResponse;

@Service
public class FollowService {

	private FollowRepository followRepository;
	private UserService userService;

	public FollowService(FollowRepository followRepository, UserService userService) {
		this.followRepository = followRepository;
		this.userService = userService;
	}
	
	public void follow(FollowCreateRequest request) {
		User user = userService.getUser(request.getUserId());
		User followed = userService.getUser(request.getFollowId());
		if(!checkFollow(user.getId(), followed.getId())) {
			Follow follow = new Follow();
			follow.setUser(user);
			follow.setFollowed(followed);
			followRepository.save(follow);
		}
		
	}
	
	public void unfollow(Long userId, Long followedId) {		
		
		followRepository.deleteByFollowedIdAndUserId(followedId,userId);
	}
	
	public List<UserResponse> followings (Long userId){
		List<Long> followingIds =  followRepository.getFollowingIds(userId);
		List<User> users = userService.getUsersByIds(followingIds);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		String currentUserName = auth.getName();
		User user = userService.getUserByUserName(currentUserName); 
		
		return users.stream().map(p -> { 
			Boolean followed = false;
			if(user!=null) {
				 followed=checkFollow(user.getId(), p.getId());	
			}
			
			return new UserResponse(p.getId(),p.getUsername(), followed);}).collect(Collectors.toList());
	}
	
	public List<UserResponse> followers (Long userId){
		List<Long> followersIds =  followRepository.getFollowerIds(userId);
		List<User> users =  userService.getUsersByIds(followersIds);
		
		return users.stream().map(p -> { 
			Boolean followed = true;
			
			return new UserResponse(p.getId(),p.getUsername(), followed);}).collect(Collectors.toList());
	}
	
	public Boolean checkFollow(Long userId, Long followedId) {
		if(userId==followedId) {
			return false;
		}
		List<Follow> follow = followRepository.checkFollow(userId, followedId);
		return !follow.isEmpty();
	}

	public List<UserResponse> suggests(Long userId) {
		int limit = 100;
		List<Long> followings =  followRepository.getFollowingIds(userId);
		
		Map<Long, Integer> usersCounts = new HashMap<>();
		List<Long> excepts = new ArrayList<Long>();
		excepts.add(userId);
		excepts.addAll(followings);
		
		
		for (Long item : followings) {
			List<Long> uFollowings = followRepository.getFollowingIds(item);
			for (Long item2 : uFollowings) {
				if(!excepts.contains(item2)) {
					usersCounts.put(item2, usersCounts.getOrDefault(item2, 0) + 1);
				}				
			}
		}
			
		List<Long> mostFrequentUsers = usersCounts.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.limit(limit)
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
		
		List<UserResponse>	suggestUsers = new ArrayList<UserResponse>();
		
		suggestUsers.addAll(mostFrequentUsers.stream().map(uid->{
			User user =userService.getUser(uid);
			return new UserResponse(user.getId(),user.getUsername(), false);
		}).collect(Collectors.toList()));
		if(suggestUsers.size()<limit) {
			suggestUsers.addAll(userService.getUsersRandom(limit-suggestUsers.size(), excepts ).stream().map(u->{
				return new UserResponse(u.getId(), u.getUsername(), false);
			}).collect(Collectors.toList()));
		}
			
		return suggestUsers;
	}
}
