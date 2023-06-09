package com.project.cikker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.cikker.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	RefreshToken findByUserId(Long userId);

}
