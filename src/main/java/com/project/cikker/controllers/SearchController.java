package com.project.cikker.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.cikker.entities.User;
import com.project.cikker.responses.PostResponse;
import com.project.cikker.responses.SearchResponse;
import com.project.cikker.services.PostService;
import com.project.cikker.services.UserService;

@RestController
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private PostService postService;
	@Autowired
	private UserService userService;
	
	@GetMapping
	public List<PostResponse> search(@RequestParam(name="q") String keyword) {
		List<PostResponse> posts = postService.findPostsByHashtags(keyword);
		return posts;
	}
	
	@GetMapping("/{keyword}")
	public ResponseEntity<List<SearchResponse>> searchUser(@PathVariable String keyword) {
		
		if(keyword.charAt(0)=='@') {
			keyword = keyword.substring(1);
			List<User> users = userService.findUsersByUsername(keyword);
			List<SearchResponse> res = users.stream().map(u->{
					return new SearchResponse(u.getId(), u.getUsername(), "user");	
					}).collect(Collectors.toList());
			return ResponseEntity.ok(res);
		}else {
			List<String> tags = postService.getTrendsAll(keyword);
			List<SearchResponse> res = tags.stream().map(t->{
					return new SearchResponse((long) tags.indexOf(t), t, "tag");	
					}).collect(Collectors.toList());
			return ResponseEntity.ok(res);
		}
	}
}
