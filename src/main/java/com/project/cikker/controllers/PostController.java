package com.project.cikker.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

import com.project.cikker.entities.Post;
import com.project.cikker.responses.PostResponse;
import com.project.cikker.responses.StatusMessageResponse;
import com.project.cikker.services.PostService;
import com.project.cikker.services.UserService;

@RestController
@RequestMapping("/posts")
public class PostController {

	private PostService postService;
	
	public PostController(PostService postService,UserService userService) {
		this.postService = postService;
	}
	
	@GetMapping
	public List<PostResponse> getAllPosts(){	
		
		return postService.getAllPosts();
	}
	
	@PostMapping
	public PostResponse createPost(@RequestParam("user") Long user, @RequestParam("text") String text, @RequestParam(value="rt_id", required=false) Long rtId, @RequestParam(value="images", required=false) MultipartFile[] images) {
		return postService.save(user, text, rtId, images);
	}
	
	@GetMapping("/{postId}")
	public PostResponse getPost(@PathVariable Long postId){
		return postService.getPost(postId);
	}
	
	@PutMapping("/{postId}")
	public PostResponse updatePost(@PathVariable Long postId, @RequestBody Post newPost){
		return postService.updatePost(postId, newPost);
	}
	
	@DeleteMapping("/{postId}")
	public StatusMessageResponse deletePost(@PathVariable Long postId){
		return postService.deletePost(postId);
	}
	@GetMapping("/trends")
	public List<String> getTrends(){
		return postService.getTrends();
	}
	
	@GetMapping("/today")
    public ResponseEntity<List<Post>> getTodayPosts() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        List<Post> todayPosts = postService.findPostsByDate(today);
        return ResponseEntity.ok(todayPosts);
    }
 
    @GetMapping("/between")
    public ResponseEntity<List<Post>> getPostsBetweenDates(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Post> posts = postService.findPostsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(posts);
    }
}
