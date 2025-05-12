package com.potato.petpotatocommunity.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityStatsDto {
    private long postCount;      // 게시글 수
    private long commentCount;   // 댓글 수
    // 좋아요 수는 보류
}
