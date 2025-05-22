package com.potato.petpotatocommunity.dto.post;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateRequest {
    private String title;
    private String content;
    private String hashtagId;
//    private String groupCodeId;  // ex: "300"
//    private String codeId;       // ex: "001"
    private List<String> deletedImageUrls;
}