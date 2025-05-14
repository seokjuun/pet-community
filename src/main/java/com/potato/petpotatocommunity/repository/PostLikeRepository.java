package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.PostLike;
import com.potato.petpotatocommunity.entity.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    long countByPost_PostId(Long postId);
}
