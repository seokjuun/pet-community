package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository  extends JpaRepository<Post, Long> {
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
