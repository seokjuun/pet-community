package com.potato.petpotatocommunity.dto.friend;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class FriendResultDto {
    private Long friendId;
    private String friendName;
    private List<PetResultDto> pets;
    private int petCount;
    private LocalDateTime createdAt;
}
