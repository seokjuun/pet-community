package com.potato.petpotatocommunity.entity;

import com.potato.petpotatocommunity.entity.key.CodeKey;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "breed_id")
//    private CommonCode breed;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "groupCode", column = @Column(name = "group_code_id")),
            @AttributeOverride(name = "code", column = @Column(name = "code_id"))
    })
    private CodeKey breedCodeKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "group_code_id", referencedColumnName = "groupCode", insertable = false, updatable = false),
            @JoinColumn(name = "code_id", referencedColumnName = "code", insertable = false, updatable = false)
    })

    private Code breed;

    @Column(nullable = false, length = 50)
    private String name;

    private LocalDate birthday;

    private String info;

    private String petImage;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}