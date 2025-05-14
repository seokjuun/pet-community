package com.potato.petpotatocommunity.service;

public interface PostLikeService {
    boolean isLiked(Long postId, Long userId);
    long getLikeCount(Long postId);
    boolean toggleLike(Long postId, Long userId);
}