package com.potato.petpotatocommunity.dto.friend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class PetResultDto {
    private Long userId;
    private Long id;
    private String breed;
    private String name;
    private LocalDate birthday;
    private String imageUrl;
}
