package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.comment.CommentCreateRequest;
import com.potato.petpotatocommunity.dto.comment.CommentDetailResponse;
import com.potato.petpotatocommunity.dto.comment.CommentResultDto;
import com.potato.petpotatocommunity.dto.comment.CommentUpdateRequest;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CommentService {
    CommentResultDto getCommentsByPostId(Long postId);
    CommentResultDto createComment(CommentCreateRequest createRequest);

    @Transactional
    CommentResultDto updateComment(Long commentId, CommentUpdateRequest updateRequest);

    @Transactional
    CommentResultDto deleteComment(Long commentId, Long userId);
}
