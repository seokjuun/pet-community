package com.potato.petpotatocommunity.dto.friend;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class FriendGetResponse {
    private List<FriendResultDto> friends;
    private int count;
}
