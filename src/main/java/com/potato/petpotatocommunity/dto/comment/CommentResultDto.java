package com.potato.petpotatocommunity.dto.comment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResultDto {
    private CommentDetailResponse commentDetailResponse;
    private List<CommentDetailResponse> commentsList;
    private String result;
    private long count;
}
