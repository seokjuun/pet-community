package com.potato.petpotatocommunity.dto.post;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequest {
//    private Long userId;
    private String groupCodeId;  // ex: "300"
    private String codeId;       // ex: "001"
    private String title;
    private String content;
}