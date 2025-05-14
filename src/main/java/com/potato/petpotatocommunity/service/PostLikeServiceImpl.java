package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.entity.Post;
import com.potato.petpotatocommunity.entity.PostLike;
import com.potato.petpotatocommunity.entity.PostLikeId;
import com.potato.petpotatocommunity.entity.User;
import com.potato.petpotatocommunity.exception.PostException;
import com.potato.petpotatocommunity.repository.PostLikeRepository;
import com.potato.petpotatocommunity.repository.PostRepository;
import com.potato.petpotatocommunity.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    @Override
    public boolean isLiked(Long postId, Long userId) {
        return postLikeRepository.existsByUser_UserIdAndPost_PostId(userId, postId);

    }

    @Override
    public long getLikeCount(Long postId) {
        return postLikeRepository.countByPost_PostId(postId);
    }

    @Override
    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("존재하지 않는 게시글입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException("존재하지 않는 사용자입니다."));

        Optional<PostLike> existing = postLikeRepository.findByUser_UserIdAndPost_PostId(userId, postId);
        if (existing.isPresent()) {
            postLikeRepository.delete(existing.get());
            return false;
        } else {
            PostLike like = PostLike.builder()
                    .post(post)
                    .user(user)
                    .id(new PostLikeId(user.getUserId(), post.getPostId()))
                    .build();
            postLikeRepository.save(like);
            return true;
        }
    }
}