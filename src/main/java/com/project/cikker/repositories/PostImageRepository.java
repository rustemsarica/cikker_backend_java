package com.project.cikker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.cikker.entities.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

}
