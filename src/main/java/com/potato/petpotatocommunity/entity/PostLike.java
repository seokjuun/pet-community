package com.potato.petpotatocommunity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLike {

    @EmbeddedId
    private PostLikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;

    @CreationTimestamp
    private LocalDateTime createdAt;
}