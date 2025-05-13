package com.potato.petpotatocommunity.controller;

import com.potato.petpotatocommunity.dto.comment.CommentCreateRequest;
import com.potato.petpotatocommunity.dto.comment.CommentResultDto;
import com.potato.petpotatocommunity.dto.comment.CommentUpdateRequest;
import com.potato.petpotatocommunity.dto.comment.CommentLikeResultDto;
import com.potato.petpotatocommunity.service.CommentLikeService;
import com.potato.petpotatocommunity.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    @GetMapping("/posts/{postId}")
    public ResponseEntity<CommentResultDto> getComments(@PathVariable Long postId,
                                                       @RequestParam(required = false) Long userId) {
        CommentResultDto resultDto = commentService.getCommentsByPostId(postId);

        if (resultDto.getCommentsList() != null) {
            for (var c : resultDto.getCommentsList()) {
                c.setLikeCount(commentLikeService.countLikes(c.getCommentId()));
                if (userId != null) {
                    c.setLiked(commentLikeService.isLiked(c.getCommentId(), userId));
                } else {
                    c.setLiked(false);
                }
            }
        }

        return ResponseEntity.ok(resultDto);
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

    @PostMapping("/{commentId}/like")
    public ResponseEntity<CommentLikeResultDto> toggleLike(@PathVariable Long commentId,
                                             @RequestParam Long userId) {
        boolean liked = commentLikeService.toggleLike(commentId, userId);
        CommentLikeResultDto resultDto = new CommentLikeResultDto();

        if (liked) {
            resultDto.setResult("좋아요 추가");
        } else {
            resultDto.setResult("좋아요 취소");
        }

        return ResponseEntity.ok(resultDto);
    }
}
