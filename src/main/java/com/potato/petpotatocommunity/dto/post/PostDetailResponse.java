package com.potato.petpotatocommunity.dto.post;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailResponse {
    private Long postId;
    private String title;
    private String content;
    private int viewCount;
    private String categoryName;
    private String codeId;
    private String groupCodeId;
    private String username;
    private Long userId;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
    private boolean isLiked;
    private Long likeCount;
}