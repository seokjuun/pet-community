package com.potato.petpotatocommunity.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 마이페이지 활동내역에서 사용자의 게시글 목록을 표시하기 위한 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListDto {
    private Long postId;         // 게시글 ID
    private String title;        // 게시글 제목
    private String hashtagName;  // 게시글 카테고리(해시태그)
    private int viewCount;       // 조회수
    private int likeCount;       // 좋아요 수
    private int commentCount;    // 댓글 수
    private LocalDateTime createdAt; // 작성일

    // 시간 표시를 위한 헬퍼 메소드
    public String getTimeAgo() {
        LocalDateTime now = LocalDateTime.now();
        long days = java.time.Duration.between(createdAt, now).toDays();

        if (days > 30) {
            return createdAt.getMonthValue() + "월 " + createdAt.getDayOfMonth() + "일";
        } else if (days > 0) {
            return days + "일 전";
        } else {
            long hours = java.time.Duration.between(createdAt, now).toHours();
            if (hours > 0) {
                return hours + "시간 전";
            } else {
                long minutes = java.time.Duration.between(createdAt, now).toMinutes();
                if (minutes > 0) {
                    return minutes + "분 전";
                } else {
                    return "방금 전";
                }
            }
        }
    }
}
