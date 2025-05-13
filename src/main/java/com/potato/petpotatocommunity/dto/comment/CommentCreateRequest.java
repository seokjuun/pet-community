package com.potato.petpotatocommunity.dto.comment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateRequest {
    private Long postId;
    private Long parentCommentId; // 대댓글일 경우
    private String content;
    private Long userId; // 로그인 완성 전
}