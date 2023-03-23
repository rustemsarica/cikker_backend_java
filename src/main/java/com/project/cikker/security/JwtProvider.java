package com.project.cikker.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;



@Component
public class JwtProvider {
	@Value("${todoapp-spring.public.key}")
	private String APP_SECRET;
	@Value("${todoapp-spring.secret.expires}")
	private Long EXPIRES_IN;
	private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	

	public String generateJwtToken(Authentication auth) {
		/* String encodedString = Base64.getEncoder().encodeToString(APP_SECRET.getBytes());
		byte[] encodedKey = Base64.getDecoder().decode(encodedString);
		Key key = new SecretKeySpec(encodedKey,0,encodedKey.length, "HmacSHA256"); */
		
		JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
		Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
		try {
			String token = Jwts.builder().setSubject(Long.toString(userDetails.getId()))
				.setIssuedAt(new Date()).setExpiration(expireDate).signWith(key)
				.compact();
			return token;
		}catch(Exception e) {
			return e.getMessage();
		}
		
	}
	
	public String generateJwtTokenByUserId(Long userId) {		
		
		Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
		return Jwts.builder().setSubject(Long.toString(userId))
				.setIssuedAt(new Date()).setExpiration(expireDate)
				.signWith(key).compact();
	}
	
	Long getUserIdFromJwt(String token) {
		
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		System.out.println(claims.getSubject());
		return Long.parseLong(claims.getSubject());
	}
	
	boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return !isTokenExpired(token);
		} catch (JwtException e) {
            return false;
		} catch (IllegalArgumentException e) {
            return false;
        }
	}

	private boolean isTokenExpired(String token) {
		Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
		return expiration.before(new Date());
	}

}
