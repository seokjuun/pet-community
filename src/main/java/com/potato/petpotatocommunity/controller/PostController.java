package com.potato.petpotatocommunity.controller;

import com.potato.petpotatocommunity.dto.post.PostCreateRequest;
import com.potato.petpotatocommunity.dto.post.PostDetailResponse;
import com.potato.petpotatocommunity.dto.post.PostResultDto;
import com.potato.petpotatocommunity.dto.post.PostUpdateRequest;
import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.entity.Post;
import com.potato.petpotatocommunity.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<PostResultDto> createPost(
            @RequestPart("post") PostCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            HttpSession session) {
        UserDto user = (com.potato.petpotatocommunity.dto.user.UserDto) session.getAttribute("user");
        return ResponseEntity.ok(postService.createPost(request, images, user));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResultDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResultDto> updatePost(
            @PathVariable Long postId,
            @RequestPart("post") PostUpdateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        return ResponseEntity.ok(postService.updatePost(postId, request, images, user));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<PostResultDto> deletePost(@PathVariable Long postId, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        return ResponseEntity.ok(postService.deletePost(postId, user));
    }

    @GetMapping
    public ResponseEntity<PostResultDto> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(postService.getPosts(page, size, keyword));
    }

    // 25-05-13 인기글
//    @GetMapping("/popular")
//    public ResponseEntity<List<Post>> getPopularPosts(@RequestParam(defaultValue = "10") int limit) {
//        List<Post> popularPosts = postService.getPopularPosts(limit);
//        return ResponseEntity.ok(popularPosts);
//    }
    @GetMapping("/popular")
    public ResponseEntity<Page<PostDetailResponse>> getPopularPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        return ResponseEntity.ok(postService.getPopularPosts(page, size));
    }

    @GetMapping("/popular/hashtag/{hashtagId}")
    public ResponseEntity<Page<PostDetailResponse>> getPopularPostsByHashtag(
            @PathVariable String hashtagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        return ResponseEntity.ok(postService.getPopularPostsByHashtag(hashtagId, page, size));
    }

}