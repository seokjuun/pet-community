package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.post.PostCreateRequest;
import com.potato.petpotatocommunity.dto.post.PostDetailResponse;
import com.potato.petpotatocommunity.dto.post.PostUpdateRequest;
import com.potato.petpotatocommunity.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    PostDetailResponse createPost(PostCreateRequest request);
    PostDetailResponse getPost(Long postId);
    PostDetailResponse updatePost(Long postId, PostUpdateRequest request);
    PostDetailResponse deletePost(Long postId);

    Page<PostDetailResponse> getPosts(int page, int size, String keyword);

    // 25-05-13 doyoen add
//    List<Post> getPopularPosts(int limit);
    Page<PostDetailResponse> getPopularPosts(int page, int size);
    Page<PostDetailResponse> getPopularPostsByHashtag(String hashtagId, int page, int size);
}