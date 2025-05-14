package com.potato.petpotatocommunity.controller;

import com.potato.petpotatocommunity.dto.comment.CommentCreateRequest;
import com.potato.petpotatocommunity.dto.comment.CommentResultDto;
import com.potato.petpotatocommunity.dto.comment.CommentUpdateRequest;
import com.potato.petpotatocommunity.dto.comment.CommentLikeResultDto;
import com.potato.petpotatocommunity.service.CommentLikeService;
import com.potato.petpotatocommunity.service.CommentService;
import com.potato.petpotatocommunity.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    @GetMapping("/posts/{postId}")
    public ResponseEntity<CommentResultDto> getComments(@PathVariable Long postId,
                                                        HttpSession session) {

        UserDto user = (UserDto) session.getAttribute("user");
        CommentResultDto resultDto = commentService.getCommentsByPostId(postId);

        if (resultDto.getCommentsList() != null) {
            for (var c : resultDto.getCommentsList()) {
                c.setLikeCount(commentLikeService.countLikes(c.getCommentId()));
                if (user != null) {
                    c.setLiked(commentLikeService.isLiked(c.getCommentId(), user.getUserId()));
                } else {
                    c.setLiked(false);
                }
            }
        }

        return ResponseEntity.ok(resultDto);
    }

    @PostMapping
    public ResponseEntity<CommentResultDto> createComment(@RequestBody CommentCreateRequest request, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(commentService.createComment(request, user));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResultDto> updateComment(@PathVariable Long commentId,
                                                          @RequestBody CommentUpdateRequest request,
                                                          HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(commentService.updateComment(commentId, request, user));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResultDto> deleteComment(@PathVariable Long commentId,
                                                          HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(commentService.deleteComment(commentId, user));
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<CommentLikeResultDto> toggleLike(@PathVariable Long commentId,
                                                           HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        boolean liked = commentLikeService.toggleLike(commentId, user.getUserId());
        CommentLikeResultDto resultDto = new CommentLikeResultDto();
        if (liked) {
            resultDto.setResult("좋아요 추가");
        } else {
            resultDto.setResult("좋아요 취소");
        }
        return ResponseEntity.ok(resultDto);
    }

}