package com.potato.petpotatocommunity.dto.comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class CommentDetailResponse {
    private Long commentId;
    private String content;
    private String username;
    private long likeCount;
    private boolean isDeleted;
    private Long parentCommentId;
    private LocalDateTime createdAt;
    private boolean isLiked;


    public CommentDetailResponse(Long commentId, String content, String username,
                                 long likeCount, boolean isDeleted,
                                 Long parentCommentId, LocalDateTime createdAt, boolean isLiked) {
        this.commentId = commentId;
        this.content = content;
        this.username = username;
        this.likeCount = likeCount;
        this.isDeleted = isDeleted;
        this.parentCommentId = parentCommentId;
        this.createdAt = createdAt;
        this.isLiked = isLiked;
    }
}