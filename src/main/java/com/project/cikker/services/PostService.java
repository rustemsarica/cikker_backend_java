package com.project.cikker.services;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.cikker.entities.Post;
import com.project.cikker.entities.User;
import com.project.cikker.repositories.PostRepository;
import com.project.cikker.responses.LikeResponse;
import com.project.cikker.responses.PostResponse;
import com.project.cikker.responses.StatusMessageResponse;

@Service
public class PostService {

	private PostRepository postRepository;
	private LikeService likeService;
	private UserService userService;
	private FollowService followService;
	private FilesStorageService filesStorageService;

	public PostService(PostRepository postRepository, UserService userService, FollowService followService, FilesStorageService filesStorageService) {
		super();
		this.postRepository = postRepository;
		this.userService = userService;
		this.followService = followService;
		this.filesStorageService = filesStorageService;
	}
	
	@Autowired
	public void setLikeService(LikeService likeService) {
		this.likeService = likeService;
	}
	
	public LikeService getLikeService() {
		return likeService;
	}
	
	public List<PostResponse> getAllPosts() {
		List<Post> list = postRepository.findAllByOrderByIdDesc();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		String currentUserName = auth.getName();
		User user = userService.getUserByUserName(currentUserName);    
		
		return list.stream().map(p -> { 
			Boolean followed = false;
			if(user!=null) {
				 followed=followService.checkFollow(user.getId(), p.getUser().getId());	
			}
			List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(p.getId()));
			return new PostResponse(p, likes,followed);}).collect(Collectors.toList());
	}

	public PostResponse save(Long userId, String text, Long rtId, MultipartFile[] images) {
		
		User user = userService.getUser(userId);
		if (user == null) {
			return null;
		}
		Post toSave = new Post();
		toSave.setText(text);
		toSave.setUser(user);
		if(rtId!=null) {
			toSave.setPost(postRepository.findById(rtId).orElse(null));
		}
		
		
		List<LikeResponse> likes=null;
		Post saved = postRepository.save(toSave);
		if(images!=null) {
			filesStorageService.savePostImage(saved, images);
		}
		
		return new PostResponse(saved, likes,false);
	}

	public PostResponse getPost(Long postId) {
		List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(postId));
		return new PostResponse(postRepository.findById(postId).orElse(null), likes,false);
	}

	public PostResponse updatePost(Long postId, Post newPost) {
		Optional<Post> post = postRepository.findById(postId);
		if (post.isPresent()) {
			Post foundedPost = post.get();
			foundedPost.setText(newPost.getText());
			postRepository.save(foundedPost);
			List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(postId));
			return new PostResponse(foundedPost, likes,false);
		} else {
			return null;
		}

	}

	public StatusMessageResponse deletePost(Long postId) {
		PostResponse post = this.getPost(postId);
		
		post.getImages().forEach(item->{
			System.out.println(item);
			filesStorageService.deleteFile(item.getUrl());
		});
		postRepository.deleteById(postId);
		StatusMessageResponse response = new StatusMessageResponse();
		response.setStatus("success");
		response.setMessage("Post deleted");
		return response;
	}

	public Post getPostById(Long postId) {
		return postRepository.findById(postId).orElse(null);
	}
	
	public List<PostResponse> getPostsByIds(List<Long> ids) {
		List<Post> list = postRepository.getPostByIdIn(ids);
		
		return list.stream().map(p -> { 
			List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(p.getId()));
			return new PostResponse(p, likes,false);}).collect(Collectors.toList());
	}
	
	public List<PostResponse> getUserPosts(Long userId) {
		List<Post> list = postRepository.findByUserIdOrderByIdDesc(userId);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		String currentUserName = auth.getName();
		User user = userService.getUserByUserName(currentUserName);    
		
		return list.stream().map(p -> { 
			Boolean followed = false;
			if(user!=null) {
				 followed=followService.checkFollow(user.getId(), p.getUser().getId());	
			}
			
			List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(p.getId()));
			return new PostResponse(p, likes,followed);}).collect(Collectors.toList());
	}
	
	public List<String> getTrends(){
		int limit = 10;
		LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        List<Post> posts = postRepository.findAllByCreatedAtAfter(today.minusDays(7));
        
        String text = posts.stream().map(Post::getText).collect(Collectors.joining(" "));
        
        Map<String, Integer> wordCounts = new HashMap<>();
        
        Pattern pattern = Pattern.compile("#\\w+");
        //Pattern pattern = Pattern.compile("(\\s|^)#[A-Za-z0-9üÜğĞıİöÖçÇşŞ]+|(\\s|^)[A-Za-z0-9üÜğĞıİöÖçÇşŞ]+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String word = matcher.group().toLowerCase().replaceAll("#", "");
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }
        
        List<String> mostFrequentWords = wordCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        
        return mostFrequentWords;
	}
	
	
	public List<Post> findPostsByDate(LocalDateTime startDate) {
        return postRepository.findAllByCreatedAtAfter(startDate);
    }
 
    public List<Post> findPostsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return postRepository.findAllByCreatedAtBetween(startDate, endDate);
    }
	
    public List<PostResponse> findPostsByHashtags(String hashtag) {
	   
    	List<Post> postsByHashtag = postRepository.findAllByTextContaining(hashtag);
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		String currentUserName = auth.getName();
		User user = userService.getUserByUserName(currentUserName);
		
		return postsByHashtag.stream().map(p -> { 
			Boolean followed = false;
			if(user!=null) {
				 followed=followService.checkFollow(user.getId(), p.getUser().getId());	
			}
			List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(p.getId()));
			return new PostResponse(p, likes,followed);}).collect(Collectors.toList());
	}
    
    public List<String> getTrendsAll(String keyword){

    	keyword = keyword.substring(1);
		LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        List<Post> posts = postRepository.findAllByCreatedAtAfter(today.minusDays(7));
        
        String text = posts.stream().map(Post::getText).collect(Collectors.joining(" "));
        
        Map<String, Integer> wordCounts = new HashMap<>();
        
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String word = matcher.group().toLowerCase().replaceAll("#", "");
            if(word.contains(keyword)) {
            	wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }            
        }
        
        List<String> mostFrequentWords = wordCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        return mostFrequentWords;
	}
}
