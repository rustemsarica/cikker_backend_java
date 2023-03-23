package com.project.cikker.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.cikker.entities.Post;
import com.project.cikker.entities.PostImage;
import com.project.cikker.repositories.PostImageRepository;

@Service
public class FilesStorageService {

	private final Path root = Paths.get("src/main/resources/static");
	
	private PostImageRepository postImageRepository;
	
	public FilesStorageService(PostImageRepository postImageRepository) {
		super();
		this.postImageRepository = postImageRepository;
	}

	public Resource saveAvatar(Long id,MultipartFile file) {
		
		Path root = Paths.get("src/main/resources/static");
		try {
			
			Files.copy(file.getInputStream(), this.root.resolve(id+".webp"));
			Path uploadedFile = root.resolve(id+".webp");			
			Resource resource = new UrlResource(uploadedFile.toUri());
			if(resource.exists() || resource.isReadable()) {
				return resource;
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		

	}
	
	public void savePostImage(Post post, MultipartFile[] files) {
		
		Path root = Paths.get("src/main/resources/static/uploads/posts");
			
		Arrays.asList(files).stream().forEach(file ->{
			try {
					Instant instant = Instant.now();
					Long timeStampMillis = instant.toEpochMilli();
					Files.copy(file.getInputStream(), root.resolve(post.getId().toString()+timeStampMillis.toString()+".webp"));
					Path uploadedFile = root.resolve(post.getId().toString()+timeStampMillis.toString()+".webp");			
					Resource resource = new UrlResource(uploadedFile.toUri());

					PostImage image = new PostImage();
					image.setPost(post);
					image.setUrl("/uploads/posts/"+resource.getFilename());
					postImageRepository.save(image);
				} catch (IOException e) {
					e.printStackTrace();
				}
		});
	}
	
	public void deleteFile(String path) {
		try {
			Path root = Paths.get("src/main/resources/static"+path); 
			Files.deleteIfExists(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
