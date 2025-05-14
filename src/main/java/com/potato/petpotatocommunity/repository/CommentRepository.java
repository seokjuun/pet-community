package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.dto.comment.CommentDetailResponse;
import com.potato.petpotatocommunity.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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

    /**
     * 사용자 ID로 댓글을 조회하고 최신순으로 정렬합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자가 작성한 댓글 목록
     */
    //List<Comment> findByUserUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.post WHERE c.user.userId = :userId ORDER BY c.createdAt DESC")
    List<Comment> findWithPostByUserUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    /**
     * 사용자가 작성한 댓글 수를 카운트합니다.
     *
     * @param userId 사용자 ID
     * @return 댓글 수
     */
    long countByUserUserId(Long userId);

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