package com.potato.petpotatocommunity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FriendshipId.class)
public class Friendship {

    @Id
    private Long userId;

    @Id
    private Long friendId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", insertable = false, updatable = false)
    private User friend;

    @CreationTimestamp
    private LocalDateTime createdAt;
}