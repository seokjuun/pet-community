package com.potato.petpotatocommunity.service;


import com.potato.petpotatocommunity.dto.comment.CommentCreateRequest;
import com.potato.petpotatocommunity.dto.comment.CommentDetailResponse;
import com.potato.petpotatocommunity.dto.comment.CommentResultDto;
import com.potato.petpotatocommunity.dto.comment.CommentUpdateRequest;
import com.potato.petpotatocommunity.entity.Comment;
import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.entity.Post;
import com.potato.petpotatocommunity.entity.User;
import com.potato.petpotatocommunity.exception.CommentException;
import com.potato.petpotatocommunity.repository.CommentRepository;
import com.potato.petpotatocommunity.repository.PostRepository;
import com.potato.petpotatocommunity.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    @Override
    public CommentResultDto getCommentsByPostId(Long postId) {
        List<CommentDetailResponse> comments = commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId);
        return CommentResultDto.builder()
                .result("success")
                .commentsList(comments)
                .count(comments.size())
                .build();
    }

    @Override
    public CommentResultDto createComment(CommentCreateRequest createRequest, UserDto userDto) {
        Post post = postRepository.findById(createRequest.getPostId())
                .orElseThrow(() -> new CommentException("게시글이 존재하지 않습니다."));

        User user = userRepository.findById(userDto.getUserId())
                .orElseThrow(() -> new CommentException("사용자가 존재하지 않습니다."));

        Comment parent = null;
        if (createRequest.getParentCommentId() != null) {
            parent = commentRepository.findById(createRequest.getParentCommentId())
                    .orElseThrow(() -> new CommentException("부모 댓글이 존재하지 않습니다."));
        }

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .parentComment(parent)
                .content(createRequest.getContent())
                .isDeleted(false)
                .build();

        commentRepository.save(comment);

        return CommentResultDto.builder()
                .result("success")
                .build();
    }


    @Override
    @Transactional
    public CommentResultDto updateComment(Long commentId, CommentUpdateRequest updateRequest, UserDto userDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException("댓글이 존재하지 않습니다."));

        if (!userDto.getUserId().equals(comment.getUser().getUserId())) {
            throw new CommentException("댓글 수정 권한이 없습니다.");
        }

        comment.setContent(updateRequest.getContent());

        return CommentResultDto.builder()
                .result("success")
                .build();
    }

    @Override
    @Transactional
    public CommentResultDto deleteComment(Long commentId, UserDto userDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException("댓글이 존재하지 않습니다."));

        if (!userDto.getUserId().equals(comment.getUser().getUserId())) {
            throw new CommentException("댓글 삭제 권한이 없습니다.");
        }

        comment.setIsDeleted(true);

        return CommentResultDto.builder()
                .result("success")
                .build();
    }


}