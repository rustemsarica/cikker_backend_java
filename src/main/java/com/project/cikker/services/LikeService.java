package com.project.cikker.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.project.cikker.entities.PostLike;
import com.project.cikker.entities.Post;
import com.project.cikker.entities.User;
import com.project.cikker.repositories.LikeRepository;
import com.project.cikker.requests.LikeCreateRequest;
import com.project.cikker.responses.LikeResponse;
import com.project.cikker.responses.PostResponse;

@Service
public class LikeService {

	private LikeRepository likeRepository;
	private UserService userService;
	private PostService postService;

	public LikeService(LikeRepository likeRepository, UserService userService,@Lazy PostService postService) {
		this.likeRepository = likeRepository;
		this.userService = userService;
		this.postService = postService;
	}
	
	public List<LikeResponse> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId) {
		List<PostLike> list;
		if(userId.isPresent() && postId.isPresent()) {
			list = likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
		}else if(userId.isPresent()) {
			list = likeRepository.findByUserId(userId.get());
		}else if(postId.isPresent()) {
			list = likeRepository.findByPostId(postId.get());
		}else
			list = likeRepository.findAll();
		return list.stream().map(like -> new LikeResponse(like)).collect(Collectors.toList());
	}

	public PostLike getOneLikeById(Long LikeId) {
		return likeRepository.findById(LikeId).orElse(null);
	}

	public PostLike createOneLike(LikeCreateRequest request) {
		Boolean check = this.checkLike(request.getPostId(), request.getUserId());
		if(check) {
			return null;
		}
		User user = userService.getUser(request.getUserId());
		Post post = postService.getPostById(request.getPostId());
		if(user != null && post != null) {
			PostLike likeToSave = new PostLike();
			likeToSave.setPost(post);
			likeToSave.setUser(user);
			return likeRepository.save(likeToSave);
		}else		
			return null;
	}

	public void deleteOneLikeById(Long likeId) {
		likeRepository.deleteById(likeId);
	}
	public Boolean checkLike(Long postId, Long userId) {
		 List<PostLike> liked = likeRepository.findByUserIdAndPostId(userId, postId);
		 return !liked.isEmpty();
	}
	
	public List<PostResponse> likedPost(Long userId){
		
		List<Long> ids = likeRepository.getPostIdsByUserId(userId);
		List<PostResponse> posts = postService.getPostsByIds(ids);
		return posts;
	}
	
}
