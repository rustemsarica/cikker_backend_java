package com.project.cikker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.cikker.entities.RefreshToken;
import com.project.cikker.entities.User;
import com.project.cikker.requests.LoginRequest;
import com.project.cikker.requests.RefreshTokenRequest;
import com.project.cikker.requests.RegisterRequest;
import com.project.cikker.responses.LoginResponse;
import com.project.cikker.responses.StatusMessageResponse;
import com.project.cikker.security.JwtProvider;
import com.project.cikker.services.RefreshTokenService;
import com.project.cikker.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private AuthenticationManager authenticationManager;
	
	private JwtProvider jwtProvider;
	
	private UserService userService;
	
	private PasswordEncoder passwordEncoder;
	
	private RefreshTokenService refreshTokenService;
	
	public AuthController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserService userService,
			PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtProvider = jwtProvider;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.refreshTokenService = refreshTokenService;
	}

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest loginRequest) {
		User user = userService.getUserByUserName(loginRequest.getUsername());
		if(user==null) {
			LoginResponse loginRes = new LoginResponse();
			loginRes.setToken("User Not Found");
			loginRes.setStatus("error");
			loginRes.setId(null);
			loginRes.setUsername(loginRequest.getUsername());
			return loginRes;
		}
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword());
		
		try {
			Authentication auth = authenticationManager.authenticate(authToken);
			SecurityContextHolder.getContext().setAuthentication(auth);
			String jwtToken = jwtProvider.generateJwtToken(auth);
			LoginResponse loginRes = new LoginResponse();
			loginRes.setToken("Bearer "+jwtToken);
			loginRes.setRefreshToken(refreshTokenService.createRefreshToken(user));
			loginRes.setStatus("success");
			loginRes.setId(user.getId());
			loginRes.setUsername(loginRequest.getUsername());
			return loginRes;
		}catch(Exception e) {
			LoginResponse loginRes = new LoginResponse();
			loginRes.setToken(e.getLocalizedMessage());
			loginRes.setStatus("error");
			loginRes.setId(null);
			loginRes.setUsername(loginRequest.getUsername());
			return loginRes;
		}
		
		
		
	}
	
	@PostMapping("/register")
	public StatusMessageResponse register(@RequestBody RegisterRequest registerRequest) {
			StatusMessageResponse res = new StatusMessageResponse();
		if(userService.getUserByUserName(registerRequest.getUsername())!=null) {
			res.setMessage("Username already in use.");
			res.setStatus("error");
			return res;
		}
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		userService.save(user);
		refreshTokenService.createRefreshToken(user);
		res.setMessage("User successfully registered.");
		res.setStatus("success");
		return res;
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
		RefreshToken token = refreshTokenService.getByUser(request.getUserId());
		LoginResponse res = new LoginResponse();
		
		if(token.getToken().equals(request.getRefreshToken()) && !refreshTokenService.isRefreshExpired(token)) {
			User user = token.getUser();
			String jwtToken = jwtProvider.generateJwtTokenByUserId(user.getId());
			res.setToken("Bearer "+jwtToken);
			res.setRefreshToken(refreshTokenService.createRefreshToken(user));
			res.setStatus("success");
			res.setId(user.getId());
			res.setUsername(user.getUsername());
			
			return ResponseEntity.ok(res);
			
		}else {
			res.setToken("Error");
			res.setStatus("error");
			res.setId(null);
			res.setUsername(null);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
		}
		
		
	}
}
