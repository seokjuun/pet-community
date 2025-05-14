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

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    @Override
    public boolean isLiked(Long postId, Long userId) {
        return postLikeRepository.existsById(new com.potato.petpotatocommunity.entity.PostLikeId(userId, postId));
    }

    @Override
    public int getLikeCount(Long postId) {
        return (int) postLikeRepository.countByPost_PostId(postId);
    }

    @Override
    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("존재하지 않는 게시글입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException("존재하지 않는 사용자입니다."));
        PostLikeId id = new PostLikeId(userId, postId);

        if (postLikeRepository.existsById(id)) {
            postLikeRepository.deleteById(id);
            post.setLikeCount(post.getLikeCount() - 1);
            return false;
        } else {
            PostLike like = new PostLike(id, user, post, null);
            postLikeRepository.save(like);
            post.setLikeCount(post.getLikeCount() + 1);
            return true;
        }
    }
}