package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.post.PostCreateRequest;
import com.potato.petpotatocommunity.dto.post.PostDetailResponse;
import com.potato.petpotatocommunity.dto.post.PostUpdateRequest;
import com.potato.petpotatocommunity.entity.CommonCode;
import com.potato.petpotatocommunity.entity.Post;
import com.potato.petpotatocommunity.entity.User;
import com.potato.petpotatocommunity.exception.PostException;
import com.potato.petpotatocommunity.repository.CommonCodeRepository;
import com.potato.petpotatocommunity.repository.PostRepository;
import com.potato.petpotatocommunity.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .result("success")
                .build();
    }

    @Override
    @Transactional
    public PostDetailResponse getPost(Long postId) {
        Post post = postRepository.findByIdWithUserAndHashtag(postId)
                .orElseThrow(() -> new PostException("존재하지 않는 게시글입니다."));

        post.setViewCount(post.getViewCount() + 1); // 조회수 증가
//        Post saved = postRepository.save(post);

        return PostDetailResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .hashtagName(post.getHashtag().getCodeName())
                .username(post.getUser().getUsername())
                .createdAt(post.getCreatedAt())
                .result("success")
                .build();
    }

    @Override
    public PostDetailResponse updatePost(Long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("존재하지 않는 게시글입니다."));

        CommonCode hashtag = commonCodeRepository.findById(request.getHashtagId())
                .orElseThrow(() -> new PostException("존재하지 않는 해시태그입니다."));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setHashtag(hashtag);

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
                .result("success")
                .build();
    }

    @Override
    public PostDetailResponse deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("존재하지 않는 게시글입니다."));

        postRepository.delete(post);

        return PostDetailResponse.builder()
                .result("success")
                .title("삭제 완료")
                .build();
    }

    @Override
    public Page<PostDetailResponse> getPosts(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Post> posts;
        if (keyword != null && !keyword.trim().isEmpty()) {
            posts = postRepository.searchByKeyword(keyword, pageable);
        } else {
            posts = postRepository.findAll(pageable);
        }

        return posts.map(post -> PostDetailResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .hashtagName(post.getHashtag().getCodeName())
                .username(post.getUser().getUsername())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .result("success")
                .build()
        );
    }
}