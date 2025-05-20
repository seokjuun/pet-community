package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.post.PostCreateRequest;
import com.potato.petpotatocommunity.dto.post.PostDetailResponse;
import com.potato.petpotatocommunity.dto.post.PostResultDto;
import com.potato.petpotatocommunity.dto.post.PostUpdateRequest;
import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
//    PostDetailResponse createPost(PostCreateRequest request);
//    PostDetailResponse getPost(Long postId);
//    PostDetailResponse updatePost(Long postId, PostUpdateRequest request);
//    PostDetailResponse deletePost(Long postId);
//
//    Page<PostDetailResponse> getPosts(int page, int size, String keyword);

    PostResultDto createPost(PostCreateRequest request, List<MultipartFile> images, UserDto userDto);
    PostResultDto getPost(Long postId, UserDto userDto);
    PostResultDto updatePost(Long postId, PostUpdateRequest request, List<MultipartFile> images,UserDto userDto);
    PostResultDto deletePost(Long postId, UserDto userDto);
    PostResultDto getPosts(int page, int size, String keyword, String hashtagId);
//    Page<PostDetailResponse> getPosts(int page, int size, String keyword);

    // 25-05-13 doyoen add
//    List<Post> getPopularPosts(int limit);
    PostResultDto getPopularPosts(int page, int size);
    PostResultDto getPopularPostsByHashtag(String hashtagId, int page, int size);
}