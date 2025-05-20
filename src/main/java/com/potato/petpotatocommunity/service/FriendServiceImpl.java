package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.friend.*;
import com.potato.petpotatocommunity.entity.*;
import com.potato.petpotatocommunity.exception.FriendException;
import com.potato.petpotatocommunity.exception.PostException;
import com.potato.petpotatocommunity.exception.UserException;
import com.potato.petpotatocommunity.repository.FriendshipRepository;
import com.potato.petpotatocommunity.repository.PetRepository;
import com.potato.petpotatocommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final PetRepository petRepository;

    @Override
    public String insertFriend(Long friendId, Long userId) {

        if (userId.equals(friendId)) {
            throw new FriendException.SelfFriendshipNotAllowed();
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new UserException.UserNotFoundException(userId);
        }
        User user = optionalUser.get();

        Optional<User> optionalFriend = userRepository.findById(friendId);
        if(optionalFriend.isEmpty()){
            throw new UserException.UserNotFoundException(friendId);
        }
        User friend = optionalFriend.get();

        FriendshipId id1 = new FriendshipId(userId, friendId);
        FriendshipId id2 = new FriendshipId(friendId, userId);

        if (friendshipRepository.existsById(id1)) {
            throw new FriendException.AlreadyExists();
        }

        // 양방향으로 친구 추가
        friendshipRepository.save(Friendship.builder().id(id1).user(user).friend(friend).build());
        friendshipRepository.save(Friendship.builder().id(id2).user(friend).friend(user).build());

        return "success";
    }

    @Override
    @Transactional
    public FriendGetResponse getFriend(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isEmpty()){
            throw new UserException.UserNotFoundException(userId);
        }

        User user = optionalUser.get();

        List<Friendship> friends = friendshipRepository.findWithFriendByUser(user);
        List<Long> friendIds = friends.stream()
                .map(f -> f.getFriend().getUserId())
                .toList();

        List<PetResultDto> allPets = petRepository.findPetByUserId(friendIds);

        Map<Long, List<PetResultDto>> petMap = allPets.stream()
                .collect(Collectors.groupingBy(PetResultDto::getUserId));

        List<FriendResultDto> friendList = friends.stream()
                .map(friend -> {
                    Long friendId = friend.getFriend().getUserId();
                    List<PetResultDto> pets = petMap.getOrDefault(friendId, Collections.emptyList());

                    return FriendResultDto.builder()
                            .friendId(friendId)
                            .friendName(friend.getFriend().getNickname())
                            .pets(pets)
                            .petCount(pets.size())
                            .createdAt(friend.getCreatedAt())
                            .build();
                })
                .toList();

        return FriendGetResponse.builder()
                .friends(friendList)
                .count(friendList.size())
                .build();
    }

    @Override
    public FriendSearchResponse searchFriend(String email, Long userId) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()){
            throw new UserException.UserNotFoundException(email);
        }

        User searchUser = optionalUser.get();

        boolean statusFriend = friendshipRepository.existsByUserIdAndFriendId(searchUser.getUserId(), userId);

        List<PetResultDto> pets = petRepository.findPetByUserId(searchUser.getUserId());

        return FriendSearchResponse.builder()
                .friendStatus(statusFriend)
                .friendId(searchUser.getUserId())
                .friendName(searchUser.getNickname())
                .pets(pets)
                .build();
    }

    @Override
    public String deleteFriend(Long friendId, Long userId) {
        FriendshipId id1 = new FriendshipId(userId, friendId);
        FriendshipId id2 = new FriendshipId(friendId, userId);

        boolean friendshipExists = friendshipRepository.existsById(id1) || friendshipRepository.existsById(id2);
        if (!friendshipExists) {
            throw new FriendException.NotFound();
        }

        // 존재하는 것만 삭제
        if (friendshipRepository.existsById(id1)) {
            friendshipRepository.deleteById(id1);
        }
        if (friendshipRepository.existsById(id2)) {
            friendshipRepository.deleteById(id2);
        }

        return "success";
    }

}
