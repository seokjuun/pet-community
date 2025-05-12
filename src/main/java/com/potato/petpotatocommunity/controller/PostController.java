package com.potato.petpotatocommunity.controller;

import com.potato.petpotatocommunity.dto.post.PostCreateRequest;
import com.potato.petpotatocommunity.dto.post.PostDetailResponse;
import com.potato.petpotatocommunity.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDetailResponse> createPost(@RequestBody PostCreateRequest request) {
        PostDetailResponse response = postService.createPost(request);
        return ResponseEntity.ok(response);
    }
}