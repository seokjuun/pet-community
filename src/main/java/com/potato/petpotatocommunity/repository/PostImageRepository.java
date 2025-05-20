package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPost_PostId(Long postId);
    void deleteByPost_PostId(Long postId);
}
