package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.entity.Comment;
import com.potato.petpotatocommunity.entity.CommentLike;
import com.potato.petpotatocommunity.entity.CommentLikeId;
import com.potato.petpotatocommunity.entity.User;
import com.potato.petpotatocommunity.exception.CommentException;
import com.potato.petpotatocommunity.repository.CommentLikeRepository;
import com.potato.petpotatocommunity.repository.CommentRepository;
import com.potato.petpotatocommunity.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService{
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public boolean toggleLike(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException("댓글이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommentException("사용자가 존재하지 않습니다."));

        Optional<CommentLike> existing = commentLikeRepository.findByUser_UserIdAndComment_CommentId(userId, commentId);
        if (existing.isPresent()) {
            commentLikeRepository.delete(existing.get());
            return false;
        } else {
            CommentLike like = CommentLike.builder()
                    .comment(comment)
                    .user(user)
                    .id(new CommentLikeId(user.getUserId(), comment.getCommentId()))
                    .build();
            commentLikeRepository.save(like);
            return true;
        }
    }

    @Override
    public boolean isLiked(Long commentId, Long userId) {
        return commentLikeRepository.existsByUser_UserIdAndComment_CommentId(userId, commentId);
    }

    @Override
    public long countLikes(Long commentId) {
        return commentLikeRepository.countByComment_CommentId(commentId);
    }
}
