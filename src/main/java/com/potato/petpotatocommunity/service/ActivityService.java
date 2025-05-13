package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.user.CommentListDto;
import com.potato.petpotatocommunity.dto.user.PostListDto;

import java.util.List;

/**
 * 사용자의 활동 내역을 조회하는 서비스 인터페이스
 */
public interface ActivityService {

    /**
     * 사용자가 작성한 게시글 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 게시글 목록
     */
    List<PostListDto> getPostsByUserId(Long userId);

    /**
     * 사용자가 작성한 댓글 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 댓글 목록
     */
    List<CommentListDto> getCommentsByUserId(Long userId);

    long countPostsByUserId(Long userId);  // 게시글 수 카운팅
    long countCommentsByUserId(Long userId);  // 댓글 수 카운팅
}
