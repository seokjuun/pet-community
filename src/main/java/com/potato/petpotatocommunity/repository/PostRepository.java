package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository  extends JpaRepository<Post, Long> {

}
