package com.potato.petpotatocommunity.service;

public interface CommentLikeService {
    boolean toggleLike(Long commentId, Long userId);
    boolean isLiked(Long commentId, Long userId);
    long countLikes(Long commentId);
}