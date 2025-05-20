package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.comment.CommentCreateRequest;
import com.potato.petpotatocommunity.dto.comment.CommentDetailResponse;
import com.potato.petpotatocommunity.dto.comment.CommentResultDto;
import com.potato.petpotatocommunity.dto.comment.CommentUpdateRequest;
import jakarta.transaction.Transactional;

import java.util.List;

import com.potato.petpotatocommunity.dto.user.UserDto;

public interface CommentService {
    CommentResultDto getCommentsByPostId(Long postId);
    CommentResultDto createComment(CommentCreateRequest createRequest, UserDto user);

    @Transactional
    CommentResultDto updateComment(Long commentId, CommentUpdateRequest updateRequest, UserDto user);

    @Transactional
    CommentResultDto deleteComment(Long commentId, UserDto user);
}
