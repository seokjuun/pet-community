package com.potato.petpotatocommunity.controller;

import com.potato.petpotatocommunity.dto.friend.FriendGetResponse;
import com.potato.petpotatocommunity.dto.friend.FriendInsertRequest;
import com.potato.petpotatocommunity.dto.friend.FriendSearchResponse;
import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.service.FriendService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;

    // 친구 요청
    @PostMapping
    public ResponseEntity<String> insertFriend(
            @RequestBody FriendInsertRequest request, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        Long userId = user.getUserId();
        String message = friendService.insertFriend(request.getFriendId(), userId);
        return ResponseEntity.ok(message);
    }

    // 친구 리스트
    @GetMapping
    public ResponseEntity<FriendGetResponse> getFriends(HttpSession session){
        UserDto user = (UserDto) session.getAttribute("user");
        Long userId = user.getUserId();
        FriendGetResponse response = friendService.getFriend(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<FriendSearchResponse> searchFriend(
            @RequestParam String email, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        Long userId = user.getUserId();
        FriendSearchResponse response = friendService.searchFriend(email, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<String> deleteFriend(
            @PathVariable Long friendId,
            HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        String message = friendService.deleteFriend(friendId, user.getUserId());
        return ResponseEntity.ok(message);
    }

}
