package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.user.CommentListDto;
import com.potato.petpotatocommunity.dto.user.PostListDto;
import com.potato.petpotatocommunity.entity.Comment;
import com.potato.petpotatocommunity.entity.Post;
import com.potato.petpotatocommunity.repository.CommentRepository;
import com.potato.petpotatocommunity.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<PostListDto> getPostsByUserId(Long userId) {
        //List<Post> posts = postRepository.findByUserUserIdOrderByCreatedAtDesc(userId);
        List<Post> posts = postRepository.findWithHashtagByUserIdOrderByCreatedAtDesc(userId);

        return posts.stream()
                .map(post -> PostListDto.builder()
                        .postId(post.getPostId())
                        .title(post.getTitle())
                        .hashtagName(post.getHashtag() != null ? post.getHashtag().getCodeName() : "기타")
                        .viewCount(post.getViewCount())
//                        .likeCount(post.getLikeCount())
                        //.likeCount(post.getPostLikes() != null ? post.getPostLikes().size() : 0)
                        .commentCount(post.getComments() != null ? post.getComments().size() : 0)
                        .createdAt(post.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentListDto> getCommentsByUserId(Long userId) {
        List<Comment> comments = commentRepository.findWithPostByUserUserIdOrderByCreatedAtDesc(userId);

        return comments.stream()
                .map(comment -> CommentListDto.builder()
                        .commentId(comment.getCommentId())
                        .postId(comment.getPost().getPostId())
                        .postTitle(comment.getPost().getTitle())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public long countPostsByUserId(Long userId) {
        return postRepository.countByUserUserId(userId); // 게시글 수 카운트
    }

    @Override
    public long countCommentsByUserId(Long userId) {
        return commentRepository.countByUserUserId(userId); // 댓글 수 카운트
    }
}
