package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.CommentLike;
import com.potato.petpotatocommunity.entity.CommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUser_UserIdAndComment_CommentId(Long userId, Long commentId);
    boolean existsByUser_UserIdAndComment_CommentId(Long userId, Long commentId);
    long countByComment_CommentId(Long commentId);
}