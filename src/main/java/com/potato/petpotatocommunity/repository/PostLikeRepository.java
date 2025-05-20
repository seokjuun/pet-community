package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.PostLike;
import com.potato.petpotatocommunity.entity.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUser_UserIdAndPost_PostId(Long userId, Long postId);
    boolean existsByUser_UserIdAndPost_PostId(Long userId, Long PostId);
    long countByPost_PostId(Long postId);
}
