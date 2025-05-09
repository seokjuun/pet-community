package com.potato.petpotatocommunity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PostLikeId.class)
public class PostLike {

    @Id
    private Long userId;

    @Id
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private Post post;

    @CreationTimestamp
    private LocalDateTime createdAt;
}