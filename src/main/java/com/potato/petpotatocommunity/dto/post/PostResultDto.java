package com.potato.petpotatocommunity.dto.post;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResultDto {
    private String result;
    private PostDetailResponse postDetailResponse;       // 단건 조회/등록/수정/삭제
    private List<PostDetailResponse> postList;           // 리스트 조회
    private Long count;                                  // 리스트 수
}