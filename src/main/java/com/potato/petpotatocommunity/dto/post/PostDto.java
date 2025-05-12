package com.potato.petpotatocommunity.dto.post;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private Long postId;
    private String title;
    private String content;
    private int viewCount;
    private String hashtagName;
    private String username;
    private LocalDateTime createdAt;
}
