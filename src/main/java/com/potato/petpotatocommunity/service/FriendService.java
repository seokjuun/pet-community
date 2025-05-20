package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.friend.FriendGetResponse;
import com.potato.petpotatocommunity.dto.friend.FriendSearchResponse;

public interface FriendService {
    String insertFriend(Long friendId, Long userId);
    FriendGetResponse getFriend(Long userId);
    FriendSearchResponse searchFriend(String email, Long userId);
    String deleteFriend(Long friendId, Long userId);
}
