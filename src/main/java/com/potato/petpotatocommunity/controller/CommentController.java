package com.potato.petpotatocommunity.controller;

import com.potato.petpotatocommunity.dto.comment.CommentCreateRequest;
import com.potato.petpotatocommunity.dto.comment.CommentResultDto;
import com.potato.petpotatocommunity.dto.comment.CommentUpdateRequest;
import com.potato.petpotatocommunity.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/posts/{postId}")
    public ResponseEntity<CommentResultDto> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @PostMapping
    public ResponseEntity<CommentResultDto> createComment(@RequestBody CommentCreateRequest request) {
        return ResponseEntity.ok(commentService.createComment(request));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResultDto> updateComment(@PathVariable Long commentId,
                                                          @RequestBody CommentUpdateRequest request) {
        return ResponseEntity.ok(commentService.updateComment(commentId, request));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResultDto> deleteComment(@PathVariable Long commentId,
                                                          @RequestParam Long userId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId, userId));
    }
}
