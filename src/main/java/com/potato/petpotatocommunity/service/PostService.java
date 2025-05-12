package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.post.PostCreateRequest;
import com.potato.petpotatocommunity.dto.post.PostDetailResponse;
import com.potato.petpotatocommunity.dto.post.PostDto;
import com.potato.petpotatocommunity.dto.post.PostUpdateRequest;
import org.springframework.data.domain.Page;

public interface PostService {
    PostDetailResponse createPost(PostCreateRequest request);
    PostDetailResponse getPost(Long postId);
    PostDetailResponse updatePost(Long postId, PostUpdateRequest request);
    PostDetailResponse deletePost(Long postId);

    Page<PostDetailResponse> getPosts(int page, int size, String keyword);
}