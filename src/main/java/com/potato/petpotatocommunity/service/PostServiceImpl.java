package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.post.PostCreateRequest;
import com.potato.petpotatocommunity.dto.post.PostDetailResponse;
import com.potato.petpotatocommunity.entity.CommonCode;
import com.potato.petpotatocommunity.entity.Post;
import com.potato.petpotatocommunity.entity.User;
import com.potato.petpotatocommunity.exception.PostException;
import com.potato.petpotatocommunity.repository.CommonCodeRepository;
import com.potato.petpotatocommunity.repository.PostRepository;
import com.potato.petpotatocommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommonCodeRepository commonCodeRepository;

    @Override
    public PostDetailResponse createPost(PostCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new PostException("존재하지 않는 사용자입니다."));

        CommonCode hashtag = commonCodeRepository.findById(request.getHashtagId())
                .orElseThrow(() -> new PostException("존재하지 않는 해시태그입니다."));

        Post post = Post.builder()
                .user(user)
                .hashtag(hashtag)
                .title(request.getTitle())
                .content(request.getContent())
                .viewCount(0)
                .likeCount(0)
                .build();

        Post saved = postRepository.save(post);

        return PostDetailResponse.builder()
                .postId(saved.getPostId())
                .title(saved.getTitle())
                .content(saved.getContent())
                .viewCount(saved.getViewCount())
                .likeCount(saved.getLikeCount())
                .hashtagName(saved.getHashtag().getCodeName())
                .username(saved.getUser().getUsername())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}