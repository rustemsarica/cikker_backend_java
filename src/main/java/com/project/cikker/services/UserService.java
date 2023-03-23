package com.project.cikker.services;

import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.cikker.entities.User;
import com.project.cikker.repositories.UserRepository;
import com.project.cikker.responses.StatusMessageResponse;

@Service
public class UserService {

	UserRepository userRepository;
	FilesStorageService filesStorageService;

	public UserService(UserRepository userRepository, FilesStorageService filesStorageService) {
		super();
		this.userRepository = userRepository;
		this.filesStorageService = filesStorageService;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User save(User newUser) {
		return userRepository.save(newUser);		
	}

	public User getUser(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}

	public User updateUser(Long userId, User newUser) {
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent()) {
			User foundUser = user.get();
			foundUser.setUsername(newUser.getUsername());
			foundUser.setPassword(newUser.getPassword());
			userRepository.save(foundUser);
			return foundUser;
		}else {
			return null;
		}
	}

	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
	}
	
	public User getUserByUserName(String username) {
		return userRepository.findByUsername(username);
	}
	
	public StatusMessageResponse updateUserAvatar(Long userId, MultipartFile file) {
		Optional<User> user = userRepository.findById(userId);
		StatusMessageResponse res = new StatusMessageResponse();
		if(user.isPresent()) {
			Resource resource = filesStorageService.saveAvatar(userId,file);
				
			if(resource!=null) {
				User foundUser = user.get();
				userRepository.save(foundUser);
				res.setStatus("success");
				res.setMessage("Profil picture updated");
			}else {
				res.setStatus("error");
				res.setMessage("We were unable to update your profile picture. Try again.");
			}
			return res;
		}else {
			res.setStatus("error");
			res.setMessage("User not found.");
			return res;
		}
	}
	
	public List<User> getUsersByIds(List<Long> ids){
		return userRepository.findByUserIdIsIn(ids);		
	}

	public List<User> findUsersByUsername(String keyword) {
		List<User> users = userRepository.findAllByUsernameContaining(keyword);	    

	    return users;
	}
	
	public List<User> getUsersRandom(int limit, List<Long> excepts){
		return userRepository.getRandomUsersWithExceptsByLimit(limit, excepts);
	}
	
	public List<User> getUsersRandom(int limit){
		return userRepository.getRandomUsersByLimit(limit);
	}
}
