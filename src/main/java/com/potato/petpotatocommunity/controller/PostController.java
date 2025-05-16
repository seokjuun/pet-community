package com.potato.petpotatocommunity.controller;

import com.potato.petpotatocommunity.dto.post.PostCreateRequest;
import com.potato.petpotatocommunity.dto.post.PostDetailResponse;
import com.potato.petpotatocommunity.dto.post.PostResultDto;
import com.potato.petpotatocommunity.dto.post.PostUpdateRequest;
import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.service.PostLikeService;
import com.potato.petpotatocommunity.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostLikeService postLikeService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<PostResultDto> createPost(
            @RequestPart("post") PostCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            HttpSession session) {
        UserDto user = (com.potato.petpotatocommunity.dto.user.UserDto) session.getAttribute("user");
        return ResponseEntity.ok(postService.createPost(request, images, user));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResultDto> getPost(@PathVariable Long postId ,HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        PostResultDto resultDto = postService.getPost(postId,user);
        PostDetailResponse detailResponse = resultDto.getPostDetailResponse();

        if (detailResponse != null){
            detailResponse.setLikeCount(postLikeService.getLikeCount(detailResponse.getPostId()));
            if (user != null){
                detailResponse.setLiked(postLikeService.isLiked(detailResponse.getPostId(), user.getUserId()));
            } else {
                detailResponse.setLiked(false);
            }
        }

        return ResponseEntity.ok(resultDto);
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
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String hashtagId
    ) {
        return ResponseEntity.ok(postService.getPosts(page, size, keyword, hashtagId));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> togglePostLike(@PathVariable Long postId, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        boolean liked = postLikeService.toggleLike(postId, user.getUserId());
        return ResponseEntity.ok(liked ? "좋아요 추가" : "좋아요 취소");
    }

    // 25-05-13 인기글
//    @GetMapping("/popular")
//    public ResponseEntity<List<Post>> getPopularPosts(@RequestParam(defaultValue = "10") int limit) {
//        List<Post> popularPosts = postService.getPopularPosts(limit);
//        return ResponseEntity.ok(popularPosts);
//    }
    @GetMapping("/popular")
    public ResponseEntity<PostResultDto> getPopularPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        return ResponseEntity.ok(postService.getPopularPosts(page, size));
    }

    @GetMapping("/popular/hashtag/{hashtagId}")
    public ResponseEntity<PostResultDto> getPopularPostsByHashtag(
            @PathVariable String hashtagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        return ResponseEntity.ok(postService.getPopularPostsByHashtag(hashtagId, page, size));
    }

}