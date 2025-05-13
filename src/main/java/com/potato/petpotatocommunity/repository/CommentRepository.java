package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.dto.comment.CommentDetailResponse;
import com.potato.petpotatocommunity.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
        SELECT new com.potato.petpotatocommunity.dto.comment.CommentDetailResponse(
            c.commentId,
            c.content,
            u.username,
            (SELECT COUNT(cl) FROM CommentLike cl WHERE cl.comment.commentId = c.commentId),
            c.isDeleted,
            c.parentComment.commentId,
            c.createdAt,
            false
        )
        FROM Comment c
        JOIN c.user u
        WHERE c.post.postId = :postId
        """)
    List<CommentDetailResponse> findAllByPostId(@Param("postId") Long postId);
}