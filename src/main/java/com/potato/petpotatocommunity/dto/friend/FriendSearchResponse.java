package com.potato.petpotatocommunity.dto.friend;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FriendSearchResponse {
    private boolean friendStatus;
    private Long friendId;
    private String friendName;
    private List<PetResultDto> pets;
}
