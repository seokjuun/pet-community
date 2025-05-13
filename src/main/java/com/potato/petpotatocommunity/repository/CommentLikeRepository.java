package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.CommentLike;
import com.potato.petpotatocommunity.entity.CommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {

}