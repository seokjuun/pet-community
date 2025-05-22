package com.potato.petpotatocommunity.entity;

import com.potato.petpotatocommunity.entity.key.CodeKey;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    @NotNull
    private String nickname;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "groupCode", column = @Column(name = "group_code_id")),
            @AttributeOverride(name = "code", column = @Column(name = "code_id"))
    })
    private CodeKey roleCodeKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "group_code_id", referencedColumnName = "groupCode", insertable = false, updatable = false),
            @JoinColumn(name = "code_id", referencedColumnName = "code", insertable = false, updatable = false)
    })

    private Code role;

    private String phone;

    private String info;

    private String profileImage;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}