package com.potato.petpotatocommunity.dto.post;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequest {
//    private Long userId;
    private String hashtagId;
    private String title;
    private String content;
}