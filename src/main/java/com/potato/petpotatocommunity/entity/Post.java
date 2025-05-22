package com.potato.petpotatocommunity.entity;

import com.potato.petpotatocommunity.entity.key.CodeKey;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private CommonCode hashtag;

    // 복합 키 code 수정
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "groupCode", column = @Column(name = "group_code_id")),
//            @AttributeOverride(name = "code", column = @Column(name = "code_id"))
//    })
//    private CodeKey hashtagCodeKey;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name = "group_code_id", referencedColumnName = "groupCode", insertable = false, updatable = false),
//            @JoinColumn(name = "code_id", referencedColumnName = "code", insertable = false, updatable = false)
//    })
//
//    private Code hashtagcode;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String content;

    private int viewCount;

//    private int likeCount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // 25-05-13 인기글 좋아요 순 정렬 기능 관련 연관관계 추가
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();

}