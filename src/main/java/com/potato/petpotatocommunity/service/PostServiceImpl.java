package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.post.PostCreateRequest;
import com.potato.petpotatocommunity.dto.post.PostDetailResponse;
import com.potato.petpotatocommunity.dto.post.PostResultDto;
import com.potato.petpotatocommunity.dto.post.PostUpdateRequest;
import com.potato.petpotatocommunity.entity.*;
import com.potato.petpotatocommunity.exception.PostException;
import com.potato.petpotatocommunity.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.potato.petpotatocommunity.dto.user.UserDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikeRepository postLikeRepository;


    @Override
    public PostResultDto createPost(PostCreateRequest request, List<MultipartFile> images, UserDto userDto) {
        User user = userRepository.findById(userDto.getUserId())
                .orElseThrow(() -> new PostException("존재하지 않는 사용자입니다."));

        CommonCode hashtag = commonCodeRepository.findById(request.getHashtagId())
                .orElseThrow(() -> new PostException("존재하지 않는 해시태그입니다."));

        Post post = Post.builder()
                .user(user)
                .hashtag(hashtag)
                .title(request.getTitle())
                .content(request.getContent())
                .viewCount(0)
//                .likeCount(0)
                .build();

        postRepository.save(post);

        if (images != null && !images.isEmpty()) {
            if (images.size() > 5) throw new PostException("이미지는 최대 5장까지 업로드 가능합니다.");

            for (MultipartFile image : images) {
                String imageUrl;
                try {
                    String uploadDir = System.getProperty("user.dir") + "/uploads/images";
                    imageUrl = saveImage(image, uploadDir);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new PostException("이미지 업로드에 실패했습니다.");
                }

                PostImage postImage = PostImage.builder()
                        .post(post)
                        .imageUrl(imageUrl)
                        .build();

                postImageRepository.save(postImage);
            }
        }

        return PostResultDto.builder()
                .result("success")
                .build();
    }

    public static String saveImage(MultipartFile image, String uploadDir) throws IOException {

        String originalName = image.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String newFileName = uuid + ext;

        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        Path filePath = path.resolve(newFileName);
        image.transferTo(filePath.toFile());

        return "/images/" + newFileName;
    }


    @Override
    @Transactional
    public PostResultDto getPost(Long postId, UserDto user) {
        Post post = postRepository.findByIdWithUserAndHashtag(postId)
                .orElseThrow(() -> new PostException("존재하지 않는 게시글입니다."));

        List<PostImage> images = postImageRepository.findByPost_PostId(postId);
        List<String> imageUrls = images.stream()
                .map(PostImage::getImageUrl)
                .toList();

        post.setViewCount(post.getViewCount() + 1); // 조회수 증가

        PostDetailResponse response = PostDetailResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .hashtagName(post.getHashtag().getCodeName())
                .hashtagId(post.getHashtag().getCodeId())
                .username(post.getUser().getUsername())
                .userId(post.getUser().getUserId())
                .createdAt(post.getCreatedAt())
                .imageUrls(imageUrls)
//                .isLiked(isLiked)
//                .likeCount(post.getLikeCount())
                .build();

        return PostResultDto.builder()
                .postDetailResponse(response)
                .result("success")
                .build();
    }

    @Override
    @Transactional
    public PostResultDto updatePost(Long postId, PostUpdateRequest request, List<MultipartFile> images, UserDto userDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("존재하지 않는 게시글입니다."));

        if (!post.getUser().getUserId().equals(userDto.getUserId())) {
            throw new PostException("게시글을 수정할 권한이 없습니다.");
        }

        CommonCode hashtag = commonCodeRepository.findById(request.getHashtagId())
                .orElseThrow(() -> new PostException("존재하지 않는 해시태그입니다."));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setHashtag(hashtag);

        postRepository.save(post);

        // Handle deleted images (delete only those which client wants to remove)
        List<String> deletedUrls = request.getDeletedImageUrls();
        if (deletedUrls != null && !deletedUrls.isEmpty()) {
            List<PostImage> toDelete = postImageRepository.findByPost_PostId(postId).stream()
                    .filter(img -> deletedUrls.contains(img.getImageUrl()))
                    .toList();
            postImageRepository.deleteAll(toDelete);
        }

        if (images != null && !images.isEmpty()) {
            if (images.size() > 5) throw new PostException("이미지는 최대 5장까지 업로드 가능합니다.");

            for (MultipartFile image : images) {
                try {
                    String uploadDir = System.getProperty("user.dir") + "/uploads/images";
                    String imageUrl = saveImage(image, uploadDir);

                    PostImage postImage = PostImage.builder()
                            .post(post)
                            .imageUrl(imageUrl)
                            .build();

                    postImageRepository.save(postImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new PostException("이미지 업로드에 실패했습니다.");
                }
            }
        }

        return PostResultDto.builder()
                .result("success")
                .build();
    }

    @Override
    public PostResultDto deletePost(Long postId, UserDto userDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("존재하지 않는 게시글입니다."));

        if (!post.getUser().getUserId().equals(userDto.getUserId())) {
            throw new PostException("게시글을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);

        return PostResultDto.builder()
                .result("success")
                .build();
    }

    @Override
    public PostResultDto getPosts(int page, int size, String keyword, String hashtagId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Post> posts;
        if (hashtagId != null && !hashtagId.trim().isEmpty()) {
            posts = postRepository.findByHashtag_CodeIdWithFetch(hashtagId, pageable);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            posts = postRepository.searchByKeyword(keyword, pageable);
        } else {
            posts = postRepository.findAll(pageable);
        }


        List<PostDetailResponse> postList = posts.stream().map(post -> PostDetailResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
//                .content(post.getContent())
                .hashtagName(post.getHashtag().getCodeName())
                .username(post.getUser().getUsername())
                .userId(post.getUser().getUserId())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .build()).toList();

        return PostResultDto.builder()
                .result("success")
                .postList(postList)
                .count(posts.getTotalElements())
                .build();
    }

    // 25-05-13 doyeon add
//    public List<Post> getPopularPosts(int limit) {
//        Pageable pageable = PageRequest.of(0, limit);
//        return postRepository.findTopPostsByLikes(pageable);
//    }

    @Override
    @Transactional
    public PostResultDto getPopularPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findPopularPostsInLast48Hours(pageable);

        posts.forEach(post -> {
            if (post.getHashtag() != null) post.getHashtag().getCodeName();
            if (post.getUser() != null) post.getUser().getUsername();
        });

        List<PostDetailResponse> postList = posts.stream().map(post -> PostDetailResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .hashtagName(post.getHashtag() != null ? post.getHashtag().getCodeName() : "Unknown")
                .username(post.getUser() != null ? post.getUser().getUsername() : "Unknown")
                .userId(post.getUser().getUserId())
                .viewCount(post.getViewCount())
//                .likeCount(post.getPostLikes().size())
                .likeCount((long) (post.getPostLikes() != null ? post.getPostLikes().size() : 0))
                .createdAt(post.getCreatedAt())
                .build()).toList();

        return PostResultDto.builder()
                .result("success")
                .postList(postList)
                .count(posts.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public PostResultDto getPopularPostsByHashtag(String hashtagId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findPopularPostsByHashtagInLast48Hours(hashtagId, pageable);

        posts.forEach(post -> {
            if (post.getHashtag() != null) post.getHashtag().getCodeName();
            if (post.getUser() != null) post.getUser().getUsername();
        });

        List<PostDetailResponse> postList = posts.stream().map(post -> PostDetailResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .hashtagName(post.getHashtag() != null ? post.getHashtag().getCodeName() : "Unknown")
                .username(post.getUser() != null ? post.getUser().getUsername() : "Unknown")
                .userId(post.getUser().getUserId())
                .viewCount(post.getViewCount())
//                .likeCount(post.getPostLikes().size())
                .likeCount((long) (post.getPostLikes() != null ? post.getPostLikes().size() : 0))
                .createdAt(post.getCreatedAt())
                .build()).toList();

        return PostResultDto.builder()
                .result("success")
                .postList(postList)
                .count(posts.getTotalElements())
                .build();
    }

}