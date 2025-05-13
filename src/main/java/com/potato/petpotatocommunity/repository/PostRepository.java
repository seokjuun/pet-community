package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 사용자 ID로 게시글을 조회하고 최신순으로 정렬합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자가 작성한 게시글 목록
     */
    // List<Post> findByUserUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * fetch join으로 hashtag 코드까지 한 번에 불러오기
     *
     * 사용자가 작성한 게시글을 가져오되,
     *
     * p.hashtag (해시태그, 카테고리 이름)
     *
     * p.comments (댓글 리스트)
     * 를 한 번의 쿼리로 로딩함 (N+1 문제 방지)
     */
//    @Query("SELECT p FROM Post p JOIN FETCH p.hashtag WHERE p.postId = :postId")
//    Optional<Post> findWithHashtagById(@Param("postId") Long postId);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.hashtag " +
            "LEFT JOIN FETCH p.comments " +
            "WHERE p.user.userId = :userId " +
            "ORDER BY p.createdAt DESC")
    List<Post> findWithHashtagByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    /**
     * 사용자가 작성한 게시글 수를 카운트합니다.
     *
     * @param userId 사용자 ID
     * @return 게시글 수
     */
    long countByUserUserId(Long userId);
  
    //crud
    
    @Query("SELECT p FROM Post p JOIN FETCH p.hashtag JOIN FETCH p.user WHERE p.postId = :postId")
    Optional<Post> findByIdWithUserAndHashtag(@Param("postId") Long postId);

    @EntityGraph(attributePaths = {"user", "hashtag"})
    @Query("""
        SELECT p FROM Post p
        WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR p.content LIKE CONCAT('%', :keyword, '%')
           OR LOWER(p.user.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Post> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "hashtag"})
    Page<Post> findAll(Pageable pageable);
  
  
    
}
