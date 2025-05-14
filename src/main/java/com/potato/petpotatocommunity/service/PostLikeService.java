package com.potato.petpotatocommunity.service;

public interface PostLikeService {
    boolean isLiked(Long postId, Long userId);
    int getLikeCount(Long postId);
    boolean toggleLike(Long postId, Long userId);
}